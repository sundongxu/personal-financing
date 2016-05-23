package com.ssdut.roysun.personalfinancialrecommendationsystem.engine;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DecimalUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by roysun on 16/5/13.
 * 推荐算法引擎
 * 股票推荐，基于乖离率BIAS：网络操作 + 本地计算 + 比较 +排序
 * ① 网络操作：数据集：自选股列表 -> 自选股各自历史收盘价（6天内、12天内、24天内） -> 一次性获取每只自选股的过去24天的收盘价，得到一个double数组 -> 6天、12天、24天的收盘价平均值
 * ② 本地计算：指标计算：公式BIAS(n)=（当前价格－n日移动平均值）/ n日移动平均值
 * ③ 比较：将每只股票的对应天数内的乖离率和超卖标准值比较，符合要求的放入结果集合
 * <p/>
 * 产品推荐，基于余弦相似度COS
 */
public class RecommendationEngine {

    public static final String TAG = "RecommendationEngine";
    public static final String URL_BASE_YAHOO = "http://ichart.yahoo.com/table.csv?";
    public static final int TERM_SHORT = 6;  // 短线天数
    public static final int TERM_MIDDLE = 12;  // 中线天数
    public static final int TERM_LONG = 24;  // 长线天数
    //    public static final int TERM_ = 60;
    public static final double BIAS_STANDARD_SHORT = -0.05;
    public static final double BIAS_STANDRAD_MIDDLE = -0.07;
    public static final double BIAS_STANDARD_LONG = -0.11;
    public static final int NUMBER_RECOMMENDATION = 5;
    public static final int NETWORK_FINISHED = 0;

    private Context mContext;
    private ArrayList<Stock> mStockList;
    private int mRequestSize;
    private TreeMap<String, Double> mMap;  // 每一只自选股对应其乖离率
    private Handler mHandler;
    private ArrayList<Stock> mResultList;
    private int count;  // 请求计数

    // RecommendationEngine for FinanceProducts，数据库操作 + 本地计算 + 排序
    public RecommendationEngine(Context context) {

    }

    // RecommendationEngine for Stocks，网络请求 + 本地计算 + 比较 + 排序
    public RecommendationEngine(Context context, ArrayList<Stock> stockList, Handler handler) {
        mContext = context;
        mStockList = stockList;
        mRequestSize = mStockList.size();
        Log.v(TAG, "请求数目=" + mRequestSize);
        mMap = new TreeMap<>();
        mHandler = handler;
        mResultList = new ArrayList<>();
        count = 0;
    }

    public void sendRequest(int dayNum) {
        new NetWorkThread(dayNum).start();
    }

    public ArrayList<Stock> getResultList() {
//        Log.v(TAG, "getResultList() invoked!!! 依据天数：" + dayNum);
//        // 另起一新线程处理网络耗时请求
//        new NetWorkThread(dayNum).start();
//        // 由自选股列表mStockList作为数据集生成推荐结List
//        ArrayList<Stock> _resultList = new ArrayList<>();
//        // 传过来的自选股列表转化为推荐结果列表
//        for (Stock _stock : mStockList) {
//            saveBiasToMap(_stock, dayNum);
//            if (isShouldRecommend(_stock, dayNum)) {
//                _resultList.add(_stock);
//            }
//        }
//        sortByBias(_resultList);
//        return _resultList;
        return mResultList;
    }

    // 雅虎股票接口
    // http://ichart.yahoo.com/table.csv?s=002322.SZ&a=08&b=25&c=2015&d=04&e=20&f=2016&g=d
    // url参数说明：
    // s：股票代码，.SZ代表深指，.SH代表沪指
    // 特别注意，a = 起始月份-1；b = 起始当天；c = 起始年份
    // 特别注意，d = 终止月份-1；e = 终止当天；f = 终止年份
    // g = 时间周期，取值在 d/m/y/v四个中选，分别表示按天、周、月、红利查询，一般取按天查询，即g=d
    // 查过去60天的数据以应对可能最多24天的股票信息请求
    public void saveBiasToMap(final Stock stock, final int dayNum) {
        count++;
        Log.v(TAG, "saveBiasToMap() invoked!!! 股票名：" + stock.getName() + "，依据天数：" + dayNum + "，请求个数：" + count);
        RequestQueue _queue = Volley.newRequestQueue(mContext);
        final String _url = URL_BASE_YAHOO + getUrlParams(stock);
        StringRequest _request = new StringRequest(Request.Method.GET, _url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponseToBias(stock, dayNum, response);
                        mRequestSize--;
                        Log.v(TAG, "回包成功！！！ 还剩" + mRequestSize + "个请求等待回包！" + "\n本次请求的url：" + _url);
                        if (mRequestSize == 0) {
                            // notifyAllRequestFinished
                            onAllRequestFinished(dayNum);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(TAG, error.toString());  // 返回服务器error
                        mStockList.remove(stock);
                        mRequestSize--;
                        if (mRequestSize == 0) {
                            // notifyAllRequestFinished
                            onAllRequestFinished(dayNum);
                        }
                    }
                });
        _queue.add(_request);
    }

    private String getUrlParams(Stock stock) {
        String _sParams = "";
        // 股票代码前缀改后缀
        String _stockType = stock.getCode().substring(0, 2);
        String _sValuePostfix = "";
        if (_stockType.equals("sh")) {
            _sValuePostfix = ".ss";
        } else if (_stockType.equals("sz")) {
            _sValuePostfix = ".sz";
        }
        String _sValue = stock.getCode().replace(_stockType, "") + _sValuePostfix;

        // 生成日期参数 a/b/c/d/e/f
        SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date _dateEnd = new Date();
        Date _dateBefore = getDateBefore(_dateEnd, 120);  // 取过去120天的数据，总能满足72天的有效数据条目
        String _sDateEnd = _dateFormat.format(_dateEnd);
        String _sDateStart = _dateFormat.format(_dateBefore);
        String[] _sEndDateList = _sDateEnd.split("/");
        String[] _sStartDateList = _sDateStart.split("/");
        // 0/1/2分别为起始时间的年/月/日
        int _aValueTmp = Integer.valueOf(_sStartDateList[1]) - 1;
        String _aValue = String.valueOf(_aValueTmp);
        String _bValue = _sStartDateList[2];
        String _cValue = _sStartDateList[0];

        int _dValueTmp = Integer.valueOf(_sEndDateList[1]) - 1;
        String _dValue = String.valueOf(_dValueTmp);
        String _eValue = _sEndDateList[2];
        String _fValue = _sEndDateList[0];

        String _gValue = "d";

        _sParams = "s=" + _sValue + "&" +
                "a=" + _aValue + "&" + "b=" + _bValue + "&" + "c=" + _cValue + "&" +
                "d=" + _dValue + "&" + "e=" + _eValue + "&" + "f=" + _fValue + "&" +
                "g=" + _gValue;

        Log.v(TAG, _sParams);
        return _sParams;
    }

    private Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    private void parseResponseToBias(Stock stock, int dayNum, String response) {
        Log.v(TAG, "parseResponseToBias() invoked!!! 股票名：" + stock.getName() + "，依据天数：" + dayNum + "回包字符串：" + response);
        String[] _infoList = response.split("\n");  // 获取股票在某一天的全部价格信息
        ArrayList<Double> _endPriceList = new ArrayList<>();
        for (int i = 0; i < dayNum + 1; i++) {
            if (i == 0) {
                continue;
            } else {
                String[] _dayInfoList = _infoList[i].split(",");
                System.out.println("收盘价：" + _dayInfoList[_dayInfoList.length - 1]);
                _endPriceList.add(Double.valueOf(_dayInfoList[_dayInfoList.length - 1]));
            }
        }
        mMap.put(stock.getCode(), calculateBias(stock, _endPriceList, dayNum));

    }

    private double calculateBias(Stock stock, ArrayList<Double> endPriceList, int dayNum) {
        Log.v(TAG, "calculateBias() invoked!!! 股票名：" + stock.getName() + "，依据天数：" + dayNum + "历史收盘价List：" + endPriceList);
        double _dBias;
        double _dSum = 0.0;
        double _dAverage;
        double _dNowPrice = stock.getNowPrice();
        for (Double _endPrice : endPriceList) {
            _dSum = DecimalUtils.add(_dSum, _endPrice);
        }
        _dAverage = DecimalUtils.div(_dSum, dayNum);
        _dBias = DecimalUtils.div(_dNowPrice - _dAverage, _dAverage);
        Log.v(TAG, "股票名为" + stock.getName() + ", dBias=" + _dBias + "乖离率bias保留5位小数=" + DecimalUtils.round(_dBias, 5) + ", dSum=" + _dSum + ", _dAverage=" + _dAverage + ", 现价=" + _dNowPrice);
        return _dBias;
    }

    private boolean isShouldRecommend(Stock stock, int dayNum) {
        double _dStandard = (dayNum == 6 ? BIAS_STANDARD_SHORT : (dayNum == 12 ? BIAS_STANDRAD_MIDDLE : BIAS_STANDARD_LONG));
        double _dBias = mMap.get(stock.getCode());
        Log.v(TAG, "isShouldRecommed() invoked!!! 股票名：" + stock.getName() + "，依据天数：" + dayNum);
        Log.v(TAG, "_dStandard=" + _dStandard);
        Log.v(TAG, "_dBias=" + _dBias);
        return _dBias <= _dStandard;
    }

    public void sortByBias(ArrayList<Stock> resultList) {
        Log.v(TAG, "sortByBias() invoked!!! 推荐股票List：" + resultList);
        Collections.sort(resultList, new Comparator<Stock>() {
            @Override
            public int compare(Stock lhs, Stock rhs) {
                // 定义股票比大小的标准：按乖离率
                return mMap.get(lhs.getCode()).compareTo(mMap.get(rhs.getCode()));
            }
        });
    }

    public void onAllRequestFinished(int dayNum) {
        for (Stock _stock : mStockList) {
            if (isShouldRecommend(_stock, dayNum)) {
                mResultList.add(_stock);
            }
        }
        sortByBias(mResultList);
        System.out.println("sdx---推荐列表股票数目=" + mResultList.size());
        mHandler.sendEmptyMessage(NETWORK_FINISHED);
    }

    private class NetWorkThread extends Thread {
        int dayNum;

        public NetWorkThread(int dayNum) {
            this.dayNum = dayNum;
        }

        @Override
        public void run() {
            super.run();
            // 传过来的自选股列表转化为推荐结果列表
            for (Stock _stock : mStockList) {
                saveBiasToMap(_stock, dayNum);
            }
        }
    }
}
