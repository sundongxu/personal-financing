package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.ImageDownloadHandler;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roysun on 16/3/12.
 * 股票查询页面，调用了新浪股票API，包含网络操作
 */
public class StockActivity extends BaseActivity {

    public final static String TAG = "StockActivity";

    private static final String sBaseUrl = "http://hq.sinajs.cn/list=";
    private static final String sImageUrl = "http://image.sinajs.cn/newchart/min/n/";

    private Button mBtnQuery;
    private EditText mStockCode;
    private ImageView mStockPic;

    private String mImageUrl;
    private Bitmap mBmp = null;

    //网络操作
    private HttpResponse mHttpResponse = null;
    private HttpEntity mHttpEntity = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                DialogUtils.showExitDialog(this, ACTIVITY_STOCK);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mStockCode = (EditText) findViewById(R.id.et_stock_code);
        mBtnQuery = (Button) findViewById(R.id.btn_stock_query);
        mStockPic = (ImageView) findViewById(R.id.iv_stock_pic);

        ((TextView) findViewById(R.id.tv_stock_query_hint)).setText(R.string.stockapp3);
        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _stockCode = mStockCode.getText().toString();
                String _url = sBaseUrl + _stockCode;
                mImageUrl = sImageUrl + _stockCode + ".gif";

                final HttpGet httpGet = new HttpGet(_url);  // 生成一个请求对象，Get方式（或Post）
                final HttpClient httpClient = new DefaultHttpClient();  // 生成一个客户端
                // 启动新线程，使用客户端发送请求，并接受网络Response数据
                new StockResponseThread(httpClient, httpGet).start();
            }
        });
    }

    List<String> mStockInfoList = new ArrayList<String>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //实际在主线程中回调该方法，回调该方法实现了非UI线程更新UI
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mStockPic.setImageBitmap(mBmp);
                    //增加size判断无效股票代码
                    if (mStockInfoList.size() > 2) {
                        ((TextView) findViewById(R.id.tv_stock_info_1)).setText("股票代码：" + mStockInfoList.get(0));
                        ((TextView) findViewById(R.id.tv_stock_info_2)).setText("股票名称：" + mStockInfoList.get(1));
                        ((TextView) findViewById(R.id.tv_stock_info_5)).setText("今日开盘价(元)：" + mStockInfoList.get(2));
                        ((TextView) findViewById(R.id.tv_stock_info_6)).setText("昨日收盘价(元)：" + mStockInfoList.get(3));
                        ((TextView) findViewById(R.id.tv_stock_info_7)).setText("当前价格 (元)：" + mStockInfoList.get(4));
                        ((TextView) findViewById(R.id.tv_stock_info_8)).setText("今日最高价(元)：" + mStockInfoList.get(5));
                        ((TextView) findViewById(R.id.tv_stock_info_9)).setText("今日最低价(元)：" + mStockInfoList.get(6));
                        ((TextView) findViewById(R.id.tv_stock_info_10)).setText("竞买价 (元)：" + mStockInfoList.get(7));
                        ((TextView) findViewById(R.id.tv_stock_info_11)).setText("竞卖价 (元)：" + mStockInfoList.get(8));
                        ((TextView) findViewById(R.id.tv_stock_info_12)).setText("成交的股票数(股)：" + mStockInfoList.get(9));
                        ((TextView) findViewById(R.id.tv_stock_info_13)).setText("成交金额 (元)：" + mStockInfoList.get(10));
                        ((TextView) findViewById(R.id.tv_stock_info_14)).setText("买一(手)：" + mStockInfoList.get(11));
                        ((TextView) findViewById(R.id.tv_stock_info_15)).setText("买一 (元)：" + mStockInfoList.get(12));
                        ((TextView) findViewById(R.id.tv_stock_info_16)).setText("买二(手)：" + mStockInfoList.get(13));
                        ((TextView) findViewById(R.id.tv_stock_info_17)).setText("买二 (元)：" + mStockInfoList.get(14));
                        ((TextView) findViewById(R.id.tv_stock_info_18)).setText("买三(手)：" + mStockInfoList.get(15));
                        ((TextView) findViewById(R.id.tv_stock_info_19)).setText("买三 (元)：" + mStockInfoList.get(16));
                        ((TextView) findViewById(R.id.tv_stock_info_20)).setText("买四(手)：" + mStockInfoList.get(17));
                        ((TextView) findViewById(R.id.tv_stock_info_21)).setText("买四 (元)：" + mStockInfoList.get(18));
                        ((TextView) findViewById(R.id.tv_stock_info_22)).setText("买五(手)：" + mStockInfoList.get(19));
                        ((TextView) findViewById(R.id.tv_stock_info_23)).setText("买五(元)：" + mStockInfoList.get(20));
                        ((TextView) findViewById(R.id.tv_stock_info_24)).setText("卖一(手)：" + mStockInfoList.get(21));
                        ((TextView) findViewById(R.id.tv_stock_info_25)).setText("卖一 (元)：" + mStockInfoList.get(22));
                        ((TextView) findViewById(R.id.tv_stock_info_26)).setText("卖二(手)：" + mStockInfoList.get(23));
                        ((TextView) findViewById(R.id.tv_stock_info_27)).setText("卖二 (元)：" + mStockInfoList.get(24));
                        ((TextView) findViewById(R.id.tv_stock_info_28)).setText("卖三(手)：" + mStockInfoList.get(25));
                        ((TextView) findViewById(R.id.tv_stock_info_29)).setText("卖三 (元)：" + mStockInfoList.get(26));
                        ((TextView) findViewById(R.id.tv_stock_info_30)).setText("卖四(手)：" + mStockInfoList.get(27));
                        ((TextView) findViewById(R.id.tv_stock_info_31)).setText("卖四 (元)：" + mStockInfoList.get(28));
                        ((TextView) findViewById(R.id.tv_stock_info_32)).setText("卖五(手)：" + mStockInfoList.get(29));
                        ((TextView) findViewById(R.id.tv_stock_info_33)).setText("卖五(元)：" + mStockInfoList.get(30));
                        ((TextView) findViewById(R.id.tv_stock_info_3)).setText("日期：" + mStockInfoList.get(31));
                        ((TextView) findViewById(R.id.tv_stock_info_4)).setText("时间：" + mStockInfoList.get(32));
                    } else {
                        Toast.makeText(StockActivity.this, "股票代码输入有误，可能原因如下："
                                + "\n" + "1.股票代码不存在" + "\n" + "2.对应股票已退市", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 新线程内部类，执行网络耗时操作
     */
    private class StockResponseThread extends Thread {
        final HttpClient httpClient;
        final HttpGet httpGet;

        StockResponseThread(HttpClient httpClient, HttpGet httpGet) {
            this.httpClient = httpClient;
            this.httpGet = httpGet;
        }

        @Override
        public void run() {
            super.run();
            try {
                mHttpResponse = httpClient.execute(httpGet);
                mHttpEntity = mHttpResponse.getEntity();
                InputStream _is;
                _is = mHttpEntity.getContent();
                BufferedReader _reader = new BufferedReader(new InputStreamReader(_is, "GBK"));  //采用GBK编码
                String _result = "";
                String _line;
                while ((_line = _reader.readLine()) != null) {
                    _result = _result + _line;
                }
                Log.v(TAG, "_result=" + _result);
                mStockInfoList.clear();  //清空股票信息
                String[] _temp = _result.split(",");
                //正则表达式匹配
                Pattern _pattern = Pattern.compile(".*_([a-z]{2}\\d{6})=\"(.*)$");  //定义模式_pattern，即要进行匹配的子串，$表示'$'前面的字符是最后一个字符(^，则是它前面字符是第一个字符)
                Matcher _matcher = _pattern.matcher(_temp[0]);
                Log.v(TAG, "_temp[0]=" + _temp[0]);
                if (_matcher.find()) {
                    //有匹配模式pattern的地方
                    //".*_([a-z]{2}\\d{6})=\"(.*)$"，该正则表达式中()内的部分称为一个group
                    mStockInfoList.add(_matcher.group(1));  //group(1):sh或sz+6位数的股票代码，代表沪指或深指，及股票代码 == mStockInfoList.get(0)
                    mStockInfoList.add(_matcher.group(2));  //group(2):股票中文名称 == mStockInfoList.get(1)
                    Log.v(TAG, "group(1)=" + _matcher.group(1));
                    Log.v(TAG, "group(2)=" + _matcher.group(2));
                    Log.v(TAG, "mStockInfoList[0]=" + mStockInfoList.get(0));
                }
                for (int i = 1; i < _temp.length; i++) {
                    mStockInfoList.add(_temp[i]);
                    Log.v(TAG, "i=" + i + ", mStockInfoList[" + i + "]=" + mStockInfoList.get(i));
                }
                mBmp = new ImageDownloadHandler().loadImageFromUrl(mImageUrl);  //网络耗时操作不可在主线程中进行
                mHandler.sendEmptyMessage(0);  //call起UI线程更新UI（回调handleMessage）
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
