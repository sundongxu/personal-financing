package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.StockInfoAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by roysun on 16/5/18.
 * 股票详情页面,volley请求K线图
 * 本机dp = px/3
 * Timer Cancel问题
 */

public class StockDetailActivity extends BaseActivity {

    public static final String TAG = "StockDetailActivity";
    public static final String URL_BASE_PIC = "http://image.sinajs.cn/newchart/min/n/";

    private Toolbar mToolbar;
    private FloatingActionButton mFabBuy;
    private RecyclerView mStockInfoList;
    private LinearLayoutManager mLayoutManager;
    private StockInfoAdapter mAdapter;
//    private ImageView mHeaderPic;
//    private ImageView mStockPic;

    private String mCode;
    //    private String mStockPicUrl;
    private ArrayList<String> mStockInfoValueList;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mCode = getIntent().getStringExtra("CODE_SELECTED");
//        mStockPicUrl = URL_BASE_PIC + mCode + ".gif";
        Log.v(TAG, "查询的股票详情代码为" + mCode);
        mStockInfoValueList = new ArrayList<>();  // 随时和股票信息列表同步的值List
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.tb_stock_detail_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("股票详情");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mStockInfoList = (RecyclerView) findViewById(R.id.rv_stock_info_list);
        mStockInfoList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStockInfoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mStockInfoList.setItemAnimator(new DefaultItemAnimator());
        mStockInfoList.setLayoutManager(mLayoutManager);
        mAdapter = new StockInfoAdapter(this);
        mStockInfoList.setAdapter(mAdapter);

//        mStockInfoList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
////                    Log.v(TAG, "mFab的当前Y坐标=" + mFabBuy.getY() + "px" + " = " + ViewUtils.px2dip(mContext, mFabBuy.getY()) + "dp");
////                    Log.v(TAG, "mFab的高度=" + mFabBuy.getHeight() + "px" + " = " + ViewUtils.px2dip(mContext, mFabBuy.getHeight()) + "dp");
////                    Log.v(TAG, "toolbar高度=" + mToolbar.getHeight() + "px" + " = " + ViewUtils.px2dip(mContext, mToolbar.getHeight()) + "dp");
//                    // 保证只有FAB在初始位置才可以点击请求K线图
//                    if (mFabBuy.getY() == 786.0) {
//                        mFabBuy.setClickable(true);
//                    } else {
//                        mFabBuy.setClickable(false);
//                    }
//                    mStockPic.setVisibility(View.GONE);
//                    mHeaderPic.setAlpha(255);  // 完全不透明，数字越小表示越透明
//                }
//                return false;
//            }
//        });

//        mStockPic = (ImageView) findViewById(R.id.iv_stock_info);
//        mHeaderPic = (ImageView) findViewById(R.id.iv_stock_detail_header_bg);

        mFabBuy = (FloatingActionButton) findViewById(R.id.fab_buy);
        mFabBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 购买股票 / 请求K线图
                buy();
//                RequestQueue _queue = Volley.newRequestQueue(mContext);
//                final LruCache<String, Bitmap> _lruCache = new LruCache<String, Bitmap>(20);
//                ImageLoader.ImageCache _imageCache = new ImageLoader.ImageCache() {
//                    @Override
//                    public Bitmap getBitmap(String key) {
//                        return _lruCache.get(key);
//                    }
//
//                    @Override
//                    public void putBitmap(String key, Bitmap value) {
//                        _lruCache.put(key, value);
//                    }
//                };
//
//                ImageLoader _imageLoader = new ImageLoader(_queue, _imageCache);
//                ImageLoader.ImageListener _imageListener = _imageLoader.getImageListener(mStockPic, R.drawable.bg_stock_detail_header, R.drawable.bg_stock_detail_header);
//                _imageLoader.get(mStockPicUrl, _imageListener);
//                mStockPic.setVisibility(View.VISIBLE);
////                mHeaderPic.setImageResource(R.drawable.bg_stock_detail_blue_temp);
//                mHeaderPic.setAlpha(0);  // 完全透明
            }
        });

        // 每隔10s更新，newTimerTask
        mTimer = new Timer("RefreshStock");  // 启动自动刷新自选股列表的定时器
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshStock();  // 相当于多线程
            }
        }, 0, 10000);
    }


    // 弹出购买多少股，根据购买股数和当前价格计算总价，然后和买家账户余额比较，余额足够则购买
    public void buy() {

    }

    public void refreshStock() {
        querySinaStock(mCode);
    }

    public void querySinaStock(final String stockCode) {
        Log.v(TAG, "要查询的股票代码为" + stockCode);
        RequestQueue _queue = Volley.newRequestQueue(this);
        String _url = StockMainActivity.URL_BASE_SINA + stockCode;
        StringRequest _request = new StringRequest(Request.Method.GET, _url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 请求成功
                        updateStockInfoListView(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 请求失败
                        ToastUtils.showMsg(mContext, "请求代码为：" + stockCode + " 的股票信息失败！");
                    }
                });

        _queue.add(_request);
    }

    public void updateStockInfoListView(String response) {

        // 回来一大串字符，这里进行解析
        mStockInfoValueList.clear();
        if (response == null || response.equals("")) {
            Log.v(TAG, "居然返回空字符？！");
            return;
        }

        String[] _leftRight = response.split("=");
        if (_leftRight.length < 2) {
            return;
        }

        String _left = _leftRight[0]; // 股票名称
        if (_left.isEmpty()) {
            return;
        }

        String _right = _leftRight[1].replaceAll("\"", "");  // 股票数据
        if (_right.isEmpty()) {
            return;
        }

        String[] values = _right.split(",");  // 每一个字符串都是一个数据
        // 计算 涨跌额 -> 计算涨跌幅
        double _dNowPrice = Double.valueOf(values[3]);
        double _dYesterdayPrice = Double.valueOf(values[2]);
        double _dIncreaseAmount = _dNowPrice - _dYesterdayPrice;
        double _dIncreasePercentage = _dIncreaseAmount / _dYesterdayPrice * 100;

        String _strNowPrice = String.format("%.3f", _dNowPrice);
        String _strIncreaseAmount = String.format("%.3f", _dIncreaseAmount);
        String _strIncreasePercentage = String.format("%.3f", _dIncreasePercentage);

        if (_dIncreaseAmount > 0) {
            // 当前价格、涨跌额、涨跌幅都应该在前面加“+”
            _strIncreaseAmount = "+" + _strIncreaseAmount;
            _strIncreasePercentage = "+" + _strIncreasePercentage + " %";
        } else if (_dIncreaseAmount < 0) {
            _strIncreaseAmount = "-" + _strIncreaseAmount;
            _strIncreasePercentage = "-" + _strIncreasePercentage + " %";
        }

        mStockInfoValueList.add(_left.split("_")[2]);  // 股票代码
        mStockInfoValueList.add(values[0]);  // 股票名称
        mStockInfoValueList.add(_strNowPrice);  // 当前价格
        mStockInfoValueList.add(_strIncreasePercentage);  // 涨跌幅
        mStockInfoValueList.add(_strIncreaseAmount);  // 涨跌额
        mStockInfoValueList.add(values[30]);  // 当前日期
        mStockInfoValueList.add(values[31]);  // 更新时间
        mStockInfoValueList.add(values[1]);   // 今日开盘价
        mStockInfoValueList.add(values[2]);   // 昨日收盘价
        mStockInfoValueList.add(values[4]);   // 今日最高价
        mStockInfoValueList.add(values[5]);   // 今日最低价
        mStockInfoValueList.add(values[6]);   // 竞买价
        mStockInfoValueList.add(values[7]);   // 竞卖价
        mStockInfoValueList.add(values[8]);   // 成交量
        mStockInfoValueList.add(values[9]);  // 成交价
        mStockInfoValueList.add(values[10]);  // 买一（手）
        mStockInfoValueList.add(values[11]);  // 买一价
        mStockInfoValueList.add(values[12]);  // 买二（手）
        mStockInfoValueList.add(values[13]);  // 买二价
        mStockInfoValueList.add(values[14]);  // 买三（手）
        mStockInfoValueList.add(values[15]);  // 买三价
        mStockInfoValueList.add(values[16]);  // 买四（手）
        mStockInfoValueList.add(values[17]);  // 买四价
        mStockInfoValueList.add(values[18]);  // 买五（手）
        mStockInfoValueList.add(values[19]);  // 买五价
        mStockInfoValueList.add(values[20]);  // 卖一（手）
        mStockInfoValueList.add(values[21]);  // 卖一价
        mStockInfoValueList.add(values[22]);  // 卖二（手）
        mStockInfoValueList.add(values[23]);  // 卖二价
        mStockInfoValueList.add(values[24]);  // 卖三（手）
        mStockInfoValueList.add(values[25]);  // 卖三价
        mStockInfoValueList.add(values[26]);  // 卖四（手）
        mStockInfoValueList.add(values[27]);  // 卖四价
        mStockInfoValueList.add(values[28]);  // 卖五（手）
        mStockInfoValueList.add(values[29]);  // 卖五价

        mAdapter.updateStockInfoValueList(mStockInfoValueList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mInputMethodManager.hideSoftInputFromInputMethod(mToolbar.getWindowToken(), 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 只有返回键会被按下需要处理
//        if (mStockPic.getVisibility() == View.VISIBLE) {
//            mStockPic.setVisibility(View.GONE);
//            mHeaderPic.setAlpha(255);
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();  // Timer真的要处理！！！
    }
}
