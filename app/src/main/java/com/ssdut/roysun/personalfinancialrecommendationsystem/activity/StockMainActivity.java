package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.StockListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.callback.ItemTouchHelperCallback;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnStartDragListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by roysun on 16/5/18.
 * 股票查询主页面,volley库，返回String
 * 异步操作打log调试最佳
 * 未登录态测试成功
 * 登录态总是提示没有admin这一列妈蛋 -> selection参数格式不对（sql语句格式问题），已解决
 * 不断restartActivity会出现多线程bug
 * Timer Cancel问题 onPause onDestroy都得cancel！！！
 * 新bug：自选股列表能否动态更新
 */
public class StockMainActivity extends BaseActivity implements OnStartDragListener {

    public static final String TAG = "StockMainActivity";
    public static final String URL_BASE_SINA = "http://hq.sinajs.cn/list=";

    private final String INDEX_SHANGHAI = "sh000001";
    private final String INDEX_SHENZHEN = "sz399001";
    private final String INDEX_SECOND_BOARD = "sz399006";

    private TextView mIndexSHText;  // 沪指
    private TextView mIndexSZText;  // 深指
    private TextView mIndexSBText;  // 创业板
    private TextView mIndexSHDeltaText;  // 沪指涨跌幅+涨跌额
    private TextView mIndexSZDeltaText;  // 深指涨跌幅+涨跌额
    private TextView mIndexSBDeltaText;  // 创业板涨跌幅+涨跌额
    private EditText mStockCodeView;  // 股票代码输入框
    private Button mBtnAdd;  // 股票添加按钮

    private RecyclerView mWatchList;
    private StockListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;

    private StockManager mStockManager;
    private ArrayList<Stock> mStockList;  // 当前用户自选股列表，未登录则为空
    //    private HashSet<String> mStockCodeSet;
    private LinkedList<String> mStockCodeList;  // 待轮询的股票代码集合，初始包含三大股指
    private boolean mIsWatch;  // 关注标志位

    private Timer mTimer;
    private RequestQueue mQueue;  // 全局请求队列对象，避免创建多个请求对象浪费资源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_main);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mStockManager = StockManager.getInstance(this);  // 获取唯一股票管理员实例
        // 之前已经获取UserManager
        if (mUserManager.isSignIn()) {
            // 登录判断
            // 有可能取到size=0但list不为空的情况，即用户没有自选股的时候
            mStockList = mStockManager.getStockListFromDB("WATCHER_NAME='" + mUserManager.getCurUser().getName() + "'"); // 这个bug找了好久啊！！！sql语句外面一定要再套一层‘’
        } else {
            // 未登录
            mStockList = new ArrayList<>();
        }
        // 初始化股票代码查询集合
        if (mStockList != null && mStockList.size() == 0) {
            mStockCodeList = new LinkedList<>();
            mStockCodeList.add(INDEX_SHANGHAI);
            mStockCodeList.add(INDEX_SHENZHEN);
            mStockCodeList.add(INDEX_SECOND_BOARD);
        } else {
            mStockCodeList = new LinkedList<>();
            mStockCodeList.add(INDEX_SHANGHAI);
            mStockCodeList.add(INDEX_SHENZHEN);
            mStockCodeList.add(INDEX_SECOND_BOARD);
            for (Stock _stock : mStockList) {
                mStockCodeList.add(_stock.getCode());
            }
        }
        mIsWatch = false;
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle("股票走势");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mIndexSHText = (TextView) findViewById(R.id.tv_index_shanghai);
        mIndexSZText = (TextView) findViewById(R.id.tv_index_shenzhen);
        mIndexSBText = (TextView) findViewById(R.id.tv_index_second_board);
        mIndexSHDeltaText = (TextView) findViewById(R.id.tv_index_shanghai_delta);
        mIndexSZDeltaText = (TextView) findViewById(R.id.tv_index_shenzhen_delta);
        mIndexSBDeltaText = (TextView) findViewById(R.id.tv_index_second_board_delta);

        mStockCodeView = (EditText) findViewById(R.id.et_stock_code);
        mBtnAdd = (Button) findViewById(R.id.btn_add_stock);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击添加 addStockToWatchList
                // 300267、002185、600519、000019、600237、、、、、、、、、、、、、、、、、
                addStockToWatchList(mStockCodeView.getText().toString());
            }
        });

        mWatchList = (RecyclerView) findViewById(R.id.rv_stock_list);
        mWatchList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWatchList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mWatchList.setItemAnimator(new DefaultItemAnimator());
        mWatchList.setLayoutManager(mLayoutManager);
        mAdapter = new StockListAdapter(mContext, this, mStockList);
        mAdapter.setStockRemovedListener(new StockListAdapter.OnStockRemovedListener() {
            @Override
            public void onStockRemoved(Stock stock) {
                // 删除某自选股的回调，通知不在请求该股票数据
                mStockCodeList.remove(stock.getCode());
                mStockList.remove(stock);
                saveStockInfoToDB(null, stock);
            }
        });
        mWatchList.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mWatchList);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  // 防止软键盘自动弹出
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer = new Timer("RefreshStocks");  // 启动自动刷新自选股列表的定时器
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshStocks();  // 相当于多线程
            }
        }, 0, 10000);
    }

    /*
    在深圳上市的股票的指数叫深指 指数代码 399001
    个股以002开头  国内股票中小企业
    沪市 在上海上市的股票 指数代码 000001
    个股以60开头   国内股票主板
    */
    private void addStockToWatchList(String stockCode) {
        // 添加股票：（1）添加到自选股列表（2）存储到数据库
        if (stockCode.length() != 6) {
            ToastUtils.showMsg(this, "股票代码至少6位！");
            return;
        }
        if (stockCode.startsWith("6")) {
            stockCode = "sh" + stockCode;
        } else if (stockCode.startsWith("0") || stockCode.startsWith("3")) {
            stockCode = "sz" + stockCode;
        } else {
            ToastUtils.showMsg(this, "股票代码不存在！");
            return;
        }
        if (!mStockCodeList.contains(stockCode)) {
            mIsWatch = true;
            mStockCodeList.add(stockCode);
            refreshStocks();  //-> 通知页面重绘
        } else {
            ToastUtils.showMsg(this, "你已关注该股！");
        }
    }

    public void refreshStocks() {
        String _codeListToQuery = "";
        for (String _code : mStockCodeList) {
            _codeListToQuery += _code;
            _codeListToQuery += ",";
        }
        querySinaStocks(_codeListToQuery);
    }

    public void querySinaStocks(String codeList) {
        Log.v(TAG, "refreshStocks invoked! 要查询的股票代码串为：" + codeList);
//        RequestQueue _queue = Volley.newRequestQueue(this);
        String _url = URL_BASE_SINA + codeList;
        StringRequest _request = new StringRequest(Request.Method.GET, _url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(TAG, "onResponse invoked! 请求成功！");
                        updateStockListView(sinaResponseToStocks(response));
                        mQueue.cancelAll(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.showMsg(mContext, "请求失败！");
                    }
                });
        _request.setTag(0);
        mQueue.add(_request);
    }

    public void updateStockListView(LinkedHashMap<String, Stock> stockMap) {
        // （1）解析这传过来的乱七八糟的String类型的response
        // （2）存储股票信息到数据库
        // （3）stockMap 存储了所有股票的信息键值对 key:股票代码，value:股票对象
        if (mStockList != null) {
            mStockList.clear();  // 每次刷新都要清空原有股票信息，无论是添加自选股的强制刷新还是每隔10s的自动刷新
        }
        Collection<Stock> _stocks = stockMap.values();
        saveStockInfoToDB(_stocks, null);  // 存储股票信息到数据库
        for (Stock _stock : _stocks) {
            if (_stock.getCode().equals(INDEX_SHANGHAI)
                    || _stock.getCode().equals(INDEX_SHENZHEN)
                    || _stock.getCode().equals(INDEX_SECOND_BOARD)) {
                String _strIndex = String.format("%.2f", _stock.getNowPrice());
                String _strIndexDelta = String.format("%.2f", _stock.getIncreasePersentage()) + "%, "
                        + String.format("%.2f", _stock.getIncreaseAmount());

                int _textColor = getResources().getColor(R.color.black);
                if (_stock.getIncreaseAmount() > 0) {
                    _strIndexDelta = "+" + _strIndexDelta;
                    _textColor = getResources().getColor(R.color.red);
                } else if (_stock.getIncreaseAmount() < 0) {
                    _strIndexDelta = "-" + _strIndexDelta;
                    _textColor = getResources().getColor(R.color.green);
                }

                if (_stock.getCode().equals(INDEX_SHANGHAI)) {
                    mIndexSHText.setText(_strIndex);
                    mIndexSHText.setTextColor(_textColor);
                    mIndexSHDeltaText.setText(_strIndexDelta);
                    mIndexSHDeltaText.setTextColor(_textColor);
                } else if (_stock.getCode().equals(INDEX_SHENZHEN)) {
                    mIndexSZText.setText(_strIndex);
                    mIndexSZText.setTextColor(_textColor);
                    mIndexSZDeltaText.setText(_strIndexDelta);
                    mIndexSZDeltaText.setTextColor(_textColor);
                } else {
                    mIndexSBText.setText(_strIndex);
                    mIndexSBText.setTextColor(_textColor);
                    mIndexSBDeltaText.setText(_strIndexDelta);
                    mIndexSBDeltaText.setTextColor(_textColor);
                }
                continue;  // 结束本轮For循环，跳至下一轮循环
            }
            mStockList.add(_stock);  // 重新添加股票信息
//            if (mIsWatch) {
//                // 关注新股操作，判重已经addToWatchList中做了
//                mIsWatch = false;  // 关注操作标志位复位
//                mStockList.add(_stock);
//                mAdapter.updateStockList(mStockList);
//                mAdapter.notifyDataSetChanged();
//                Snackbar.make(mToolbar, "股票" + _stock.getName() + "添加到我的自选股成功！",
//                        Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
//            } else {
//                // 自动刷新操作
//                mAdapter.updateStockList(mStockList);
//                mAdapter.notifyDataSetChanged();
//                Snackbar.make(mToolbar, "股票" + _stock.getName() + "自动刷新成功！",
//                        Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
//            }
        }
        // 最后再去通知Adapter更新股票列表信息
        mAdapter.updateStockList(mStockList);
        mAdapter.notifyDataSetChanged();
        if (mIsWatch) {
            mIsWatch = false;
            Snackbar.make(mToolbar, "添加到自选股成功！",
                    Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
        } else {
//            Snackbar.make(mToolbar, "自动刷新自选股列表成功！",
//                    Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
        }
    }

    /**
     * 将响应回包解析为股票代码对应股票的map集合 TreeMap<stockCode, Stock>
     *
     * @param response 回包字符串
     * @return 股票代码对应股票的map集合
     */
    public LinkedHashMap<String, Stock> sinaResponseToStocks(String response) {
        if (response == null || response.equals("")) {
            return null;
        }
        response = response.replaceAll("\n", "");
        String[] _stocks = response.split(";");  // 以";"为分隔符，不同股票

//        TreeMap<String, Stock> _stockMap = new TreeMap<>();
        LinkedHashMap<String, Stock> _stockMap = new LinkedHashMap<>();

        for (String _stock : _stocks) {
            String[] _leftRight = _stock.split("=");
            if (_leftRight.length < 2) {
                continue;
            }

            String _left = _leftRight[0]; // 股票名称
            if (_left.isEmpty()) {
                continue;
            }

            String _right = _leftRight[1].replaceAll("\"", "");  // 股票数据
            if (_right.isEmpty()) {
                continue;
            }

            Stock _stockNow = new Stock();
            String[] values = _right.split(",");  // 每一个字符串都是一个数据
            _stockNow.setCode(_left.split("_")[2]);  // ① 设置股票代码
            String _stockNowCode = _stockNow.getCode();
            if (_stockNowCode.equals(INDEX_SHANGHAI) || _stockNowCode.equals(INDEX_SHENZHEN) || _stockNowCode.equals(INDEX_SECOND_BOARD)) {
                _stockNow.setWatcherName("");
                _stockNow.setBuyNum(0);
            } else {
                // 非三大股指，必须有关注者为当前用户
                _stockNow.setWatcherName(mUserManager.getCurUser().getName());  // ② 设置股票关注者用户名
                _stockNow.setBuyNum(0);  // ③ 设置当前用户持有股数
            }
            _stockNow.setName(values[0]);  // ④ 设置股票名称
            _stockNow.setNowPrice(Double.valueOf(values[3]));  // ⑤ 设置股票当前价格

            // 计算 涨跌额 -> 计算涨跌幅
            double _dYesterdayPrice = Double.valueOf(values[2]);
            double _dIncreaseAmount = _stockNow.getNowPrice() - _dYesterdayPrice;
            double _dIncreasePercentage = _dIncreaseAmount / _dYesterdayPrice * 100;
            _stockNow.setIncreasePersentage(_dIncreasePercentage);  // ⑥ 设置股票涨跌幅
            _stockNow.setIncreaseAmount(_dIncreaseAmount);  // ⑦ 设置股票涨跌额
            // id就不用设置了，直接在saveToDB中取出对应条目的id

            _stockMap.put(_stockNow.getCode(), _stockNow);  // 存入当前解析的某只股票回包信息
        }
        return _stockMap;
    }

    /**
     * 股票信息的数据库操作，两种调用情形：
     * （1）添加/修改，点击添加按钮/自动刷新时触发
     * （2）删除，侧滑自选股列表条目时触发
     *
     * @param stocks        请求得到的最新股票信息集合
     * @param stockToDelete 待删除的股票，如果不是删除操作，值传null
     */
    public void saveStockInfoToDB(Collection<Stock> stocks, Stock stockToDelete) {
        // 数据库存储操作，需要去重，先保证三大股指不会被加入到数据库
        if (stockToDelete != null) {
            // 删除操作，只是删
            int _stockIdToDelete = mStockManager.getWatchedStockFromDB(stockToDelete.getCode(), stockToDelete.getWatcherName()).getId();  // 重组mStockList的时候没有存储其id
            if (mStockManager.deleteStockFromWatchList(_stockIdToDelete) == 1) {
                ToastUtils.showMsg(mContext, "删除数据库股票信息成功！股票名称为" + stockToDelete.getName());
            } else {
                ToastUtils.showMsg(mContext, "删除数据库股票信息失败！股票名称为" + stockToDelete.getName());
            }
        } else {
            // 非删除操作，包括增、改
            for (Stock _stock : stocks) {
                if (!_stock.getWatcherName().equals("")) {
                    // 关注者不为空时为个股，在数据库中
                    if (!mStockManager.isExistInWatchList(_stock)) {
                        // 当前用户没有关注该股
                        // 添加操作
                        if (mStockManager.watchStock(_stock) != -1) {
                            ToastUtils.showMsg(mContext, "添加数据库股票信息成功！股票名称为" + _stock.getName());
                        } else {
                            //数据库insert操作出错
                            ToastUtils.showMsg(mContext, "添加到数据库股票信息失败！股票名称为" + _stock.getName());
                        }
                    } else {
                        // 当前用户已关注该股
                        // 更新操作
                        int _stockIdToUpdate = mStockManager.getWatchedStockFromDB(_stock.getCode(), _stock.getWatcherName()).getId();  // 充足mStockList的时候没有存储其Id
                        int _iRowsAffected = mStockManager.updateStockInfo(_stock, _stockIdToUpdate, false);
                        if (_iRowsAffected == 1) {
//                            ToastUtils.showMsg(mContext, "数据库条目更新成功！股票名称为"+_stock.getName());
                        } else {
//                            ToastUtils.showMsg(mContext, "数据库条目无须更新！股票名称为" + _stock.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                break;
        }
        return true;
    }

    // 坑爹的Timer，果然是个隐患，跳转到后面的页面onPause了以后要cancel掉！！！不然会导致后续页面数据库更新操作出每次被这里的更新操作覆盖！！！
    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();  // 出栈时停止Timer
    }
}

/*
json格式示例：
回包总共包括1个大JsonObject = {"resultcode":"200","reason":"SUCCESSED","result":"[]","errorcode":"0"}
里面又包括4个小JsonObject：resultcode、reason、result、errorcode
其中第三个小JsonObject，即result中的value为一个数组：
{
	"resultcode":"200",
	"reason":"SUCCESSED!",
	"result":[
		{
			"data":{
				"buyFive":"0",
				"buyFivePri":"0",
				"buyFour":"0",
				"buyFourPri":"0",
				"buyOne":"0",
				"buyOnePri":"0",
				"buyThree":"0",
				"buyThreePri":"0",
				"buyTwo":"0",
				"buyTwoPri":"0",
				"competitivePri":"0",
				"date":"2016-05-19",
				"gid":"sh000001",
				"increPer":"0.02",
				"increase":"0.4890",
				"name":"上证指数",
				"nowPri":"2808.0031",
				"reservePri":"0",
				"sellFive":"0",
				"sellFivePri":"0",
				"sellFour":"0",
				"sellFourPri":"0",
				"sellOne":"0",
				"sellOnePri":"0",
				"sellThree":"0",
				"sellThreePri":"0",
				"sellTwo":"0",
				"sellTwoPri":"0",
				"time":"14:47:43",
				"todayMax":"2829.4023",
				"todayMin":"2801.5475",
				"todayStartPri":"2802.3147",
				"traAmount":"113325031302",
				"traNumber":"1019437",
				"yestodEndPri":"2807.5141"
			},
			"dapandata":{
				"dot":"2808.0031",
				"name":"上证指数",
				"nowPic":"0.4890",
				"rate":"0.02",
				"traAmount":"11332503",
				"traNumber":"1019437"
			},
			"gopicture":{
				"minurl":"http://image.sinajs.cn/newchart/min/n/sh000001.gif",
				"dayurl":"http://image.sinajs.cn/newchart/daily/n/sh000001.gif",
				"weekurl":"http://image.sinajs.cn/newchart/weekly/n/sh000001.gif",
				"monthurl":"http://image.sinajs.cn/newchart/monthly/n/sh000001.gif"
			}
		}
	],
	"error_code":0
}
 */