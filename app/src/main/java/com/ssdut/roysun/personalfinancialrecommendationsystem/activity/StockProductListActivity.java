package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.StockProductListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.FinanceProduct;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.FinanceProductManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/18.
 * 自选列表页面，股票或理财产品
 */
public class StockProductListActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public static final String TAG = "StockProductListActivity";

    private ImageView mHeaderPic;
    private ObservableListView mWatchedList;
    private View mListBackgroundView;
    private StockProductListAdapter mAdapter;
    private int mParallaxImageHeight;

    private boolean mIsStock;

    // 股票
    private StockManager mStockManager;
    private ArrayList<Stock> mStockList;

    // 理财产品
    private FinanceProductManager mProductManager;
    private ArrayList<FinanceProduct> mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_product_list);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mParallaxImageHeight = ViewUtils.dip2px(this, 200);

        int _listType = getIntent().getIntExtra("LIST_TYPE", 2);
        if (_listType == 0) {
            mIsStock = true;
        } else if (_listType == 1) {
            mIsStock = false;
        } else {
            // 参数错误直接finish当前页面
            finish();
        }

        if (mIsStock) {
            mStockManager = StockManager.getInstance(this);
            mStockList = mStockManager.getStockListFromDB("WATCHER_NAME='" + mUserManager.getCurUser().getName() + "'");
        } else {
            mProductManager = FinanceProductManager.getInstance(this);
            mProductList = mProductManager.getProductListFromDB("");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            if (mIsStock) {
                mToolbar.setTitle("自选股列表");  // 这里根据intent传来的参数判断是自选股列表还是
            } else {
                mToolbar.setTitle("理财产品列表");
            }
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.holo_blue_bright)));
        mHeaderPic = (ImageView) findViewById(R.id.iv_bg_header_mylist);
        mWatchedList = (ObservableListView) findViewById(R.id.ol_recommendation_result_list);
        mWatchedList.setScrollViewCallbacks(this);
        View paddingView = new View(this);
        AbsListView.LayoutParams lp =
                new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mParallaxImageHeight);
        paddingView.setLayoutParams(lp);
        paddingView.setClickable(true);
        mWatchedList.addHeaderView(paddingView);
        if (mIsStock) {
            mAdapter = new StockProductListAdapter(this, mStockList, mStockManager, null, null);
        } else {
            mAdapter = new StockProductListAdapter(this, null, null, mProductList, mProductManager);
        }
        mWatchedList.setAdapter(mAdapter);
        mWatchedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里因为ListView有一个HeaderView，所以条目点击相应位置position从1开始
                // 跳转到详情页
                if (mIsStock) {
                    Intent _intent = new Intent(mContext, StockDetailActivity.class);
                    _intent.putExtra("CODE_SELECTED", mStockList.get(position - 1).getCode());
                    startActivity(_intent);
                } else {
                    // 加载推荐引擎
                    Intent _intent = new Intent(mContext, FinanceProductDetailActivity.class);
                    _intent.putExtra("NAME_SELECTED", mProductList.get(position - 1).getName());
                    startActivity(_intent);
                }
            }
        });
        mListBackgroundView = findViewById(R.id.list_background);
        if (mIsStock) {
            Snackbar.make(mToolbar, "共计" + mStockList.size() + "只自选股加载成功！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mWatchedList.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.holo_blue_bright);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        com.nineoldandroids.view.ViewHelper.setTranslationY(mHeaderPic, -scrollY / 2);
        com.nineoldandroids.view.ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mParallaxImageHeight));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }
}
