package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;

public class StockTradeActivity extends BaseActivity {

    public static final String TAG = "StockTradeActivity";

    public static final String URL_BASE_PIC = "http://image.sinajs.cn/newchart/min/n/";

    private TextView mStockCodeText;  // 正在交易的股票代码
    private TextView mStockNameText;  // 正在交易的股票名称
    private TextView mBalanceText;  // 当前账户余额
    private TextView mOwnNumText;  // 持有的的股数
    private EditText mSellNumView;  // 输入卖出的股数
    private Button mBtnSell;  // 卖出按钮
    private EditText mBuyNumView;  // 输入买进的股数
    private Button mBtnBuy;  // 买进按钮
    private ImageView mStockPic;
    private ProgressBar mProgressBar;

    private String mCode;
    private Stock mStock;
    private String mStockPicUrl;
    private StockManager mStockManager;
    private double mNowPrice;
    private User mCurUser;
    private double mBalance;
    private int mOwnNum;
    private int MAX_BUY_NUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_trade);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mCurUser = mUserManager.getCurUser();
        mCode = getIntent().getStringExtra("CODE");
        mStockManager = StockManager.getInstance(this);
        mStock = mStockManager.getWatchedStockFromDB(mCode, mCurUser.getName());
        mNowPrice = mStock.getNowPrice();
        mStockPicUrl = URL_BASE_PIC + mCode + ".gif";
        mBalance = mCurUser.getBalance();
        mOwnNum = mStock.getBuyNum();
        MAX_BUY_NUM = (int) (mBalance / mStock.getNowPrice());
        Log.v(TAG, "账户余额=" + mBalance + ", 当前股价=" + mStock.getNowPrice() + ", 最大可买进股数=" + MAX_BUY_NUM);

//        ArrayList<Stock> _list = mStockManager.getStockListFromDB("");
//        Log.v(TAG, "Stock表条目总数为：" + String.valueOf(_list.size()));
//        for (Stock _stock : _list) {
//            Log.v(TAG, "Stock表条目位置（2）：" + "\n"
//                    + "股票名称：" + _stock.getName() + "\n"
//                    + "代码：" + _stock.getCode() + "\n"
//                    + "关注用户名：" + _stock.getWatcherName() + "\n"
//                    + "持有数量：" + _stock.getBuyNum() + "\n");
//        }
//        Log.v(TAG, "条目 StockManager:" + mStockManager);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle("股票交易");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mStockNameText = (TextView) findViewById(R.id.tv_stock_name);
        mStockNameText.setText(mStock.getName());
        mStockCodeText = (TextView) findViewById(R.id.tv_stock_code);
        mStockCodeText.setText(mCode);
        mBalanceText = (TextView) findViewById(R.id.tv_balance);
        mBalanceText.setText(String.format("%.2f", mBalance));
        mOwnNumText = (TextView) findViewById(R.id.tv_own_number);
        mOwnNumText.setText(String.valueOf(mOwnNum));
        mBuyNumView = (EditText) findViewById(R.id.et_buy_num);
        mSellNumView = (EditText) findViewById(R.id.et_sell_num);
        mStockPic = (ImageView) findViewById(R.id.iv_stock_pic);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mBtnBuy = (Button) findViewById(R.id.btn_buy_in);
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();  // 实际就是数据库操作 增加
            }
        });

        mBtnSell = (Button) findViewById(R.id.btn_sell_out);
        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sell();  // 实际就是数据库操作 删除
            }
        });
        loadStockKPic();
    }

    private void buy() {
        if (!mBuyNumView.getText().toString().equals("")) {
            int _iBuyNum = Integer.valueOf(mBuyNumView.getText().toString());
            if (_iBuyNum <= MAX_BUY_NUM) {
                if (mStock != null) {
                    // 从来没有购买过，但是之前关注过，所以watcherName不用设置，直接设置buyNum即可
                    mStock.setBuyNum(mStock.getBuyNum() + _iBuyNum);
                    mBalance = mBalance - (mNowPrice * _iBuyNum);
                    mCurUser.setBalance(mBalance);
                    mUserManager.updateUserInfo(mCurUser, mCurUser.getId());  // 更新账户余额信息
                    if (mStockManager.buyStock(mStock) == 1) {
                        // buy操作相当于数据库更新操作
                        Snackbar.make(mToolbar, "买进" + mStock.getName() + _iBuyNum + "股成功！", Snackbar.LENGTH_LONG).show();

                        MAX_BUY_NUM = MAX_BUY_NUM - _iBuyNum;

                        mStock = mStockManager.getWatchedStockFromDB(mCode, mStock.getWatcherName());  // 看看数据库里股票条目是不是被修改了
                        mOwnNumText.setText(String.valueOf(mStock.getBuyNum()));

                        mCurUser = mUserManager.getUserFromDB(mCurUser.getName());  // 看看数据库里账户余额是不是被修改了
                        mBalanceText.setText(String.format("%.2f", mCurUser.getBalance()));

                        mBuyNumView.setText("");
                    } else {
                        // 数据库更新出错
                        Snackbar.make(mToolbar, "买进" + mStock.getName() + "失败！", Snackbar.LENGTH_LONG).show();
                    }
                }
            } else {
                Snackbar.make(mToolbar, "已超出最大可买进数量！请重新输入或充值后购买！", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(mToolbar, "请输入买进数量！", Snackbar.LENGTH_LONG).show();
        }
    }

    private void sell() {
        if (!mSellNumView.getText().toString().equals("")) {
            int _iSellNum = Integer.valueOf(mSellNumView.getText().toString());
            if (_iSellNum <= mStock.getBuyNum()) {
                // 可以卖出，小于等于临界条件要考虑嘛？可以不用
                if (mStock != null) {
                    mStock.setBuyNum(mStock.getBuyNum() - _iSellNum);
                    mBalance = mBalance + (mNowPrice * _iSellNum);
                    mCurUser.setBalance(mBalance);
                    mUserManager.updateUserInfo(mCurUser, mCurUser.getId());
                    if (mStockManager.sellStock(mStock) == 1) {
                        Snackbar.make(mToolbar, "卖出" + mStock.getName() + _iSellNum + "股成功！", Snackbar.LENGTH_LONG).show();

                        MAX_BUY_NUM = MAX_BUY_NUM + _iSellNum;

                        mStock = mStockManager.getWatchedStockFromDB(mCode, mStock.getWatcherName());  // 看看数据库里股票条目是不是被修改了
                        mOwnNumText.setText(String.valueOf(mStock.getBuyNum()));

                        mCurUser = mUserManager.getUserFromDB(mCurUser.getName());  // 看看数据库里账户余额是不是被修改了
                        mBalanceText.setText(String.format("%.2f", mCurUser.getBalance()));

                        mSellNumView.setText("");

                    } else {
                        Snackbar.make(mToolbar, "卖出" + mStock.getName() + "失败！", Snackbar.LENGTH_LONG).show();
                    }
                }
            } else {
                Snackbar.make(mToolbar, "已超出最大可卖出数量！请重新输入！", Snackbar.LENGTH_LONG).show();
            }

        } else {
            Snackbar.make(mToolbar, "请输入卖出数量！", Snackbar.LENGTH_LONG).show();
        }
    }

    private void loadStockKPic() {
        RequestQueue _queue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> _lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache _imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String key) {
                return _lruCache.get(key);
            }

            @Override
            public void putBitmap(String key, Bitmap value) {
                _lruCache.put(key, value);
            }
        };

        ImageLoader _imageLoader = new ImageLoader(_queue, _imageCache);
        ImageLoader.ImageListener _imageListener = _imageLoader.getImageListener(mStockPic, 0, 0);
        _imageLoader.get(mStockPicUrl, _imageListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
        }
        return super.onOptionsItemSelected(item);
    }
}
