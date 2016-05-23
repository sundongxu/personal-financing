package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.StockProductListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.engine.RecommendationEngine;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/18.
 * 推荐结果页面
 */
public class RecommendationResultActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    public static final String TAG = "RecommendationResultActivity";

    private FrameLayout mPageArea;
    private ImageView mHeaderPic;
    private ObservableListView mResultList;
    private View mListBackgroundView;
    private StockProductListAdapter mAdapter;
    private int mParallaxImageHeight;
    private RelativeLayout mLoadingView;
    private StockManager mStockManager;
    private ArrayList<Stock> mStockList;

    private RecommendationEngine mEngine;
    private ArrayList<Stock> mRecommendationResultList;
    private Handler mHandler;
    private int mDayNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_result);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mDayNum = getIntent().getIntExtra("PARAM_DAYS", 6);
        mContext = this;
        mParallaxImageHeight = ViewUtils.dip2px(this, 200);
        mStockManager = StockManager.getInstance(this);
        mStockList = mStockManager.getStockListFromDB("USER_NAME='" + mUserManager.getCurUser().getName() + "'");

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case RecommendationEngine.NETWORK_FINISHED:
                        mRecommendationResultList = mEngine.getResultList();
                        mAdapter = new StockProductListAdapter(mContext, mRecommendationResultList, mStockManager);
                        mResultList.setAdapter(mAdapter);
                        mPageArea.removeView(mLoadingView);
                        Snackbar.make(mToolbar, "推荐成功！共计" + mRecommendationResultList.size() + "只股票！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        break;
                }
            }
        };

        mEngine = new RecommendationEngine(this, mStockList, mHandler);  // 可能传进去的StockList.size() = 0
        mEngine.sendRequest(mDayNum);
    }

    @Override
    protected void initView() {
        super.initView();
        mPageArea = (FrameLayout) findViewById(R.id.fl_page);
        if (mToolbar != null) {
            mToolbar.setTitle("推荐结果");  // 这里根据intent传来的参数判断是自选股列表还是
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.holo_blue_bright)));
        mHeaderPic = (ImageView) findViewById(R.id.iv_bg_header_result_list);
        mResultList = (ObservableListView) findViewById(R.id.ol_stock_recommendation_result_list);
        mResultList.setScrollViewCallbacks(this);
        View paddingView = new View(this);
        AbsListView.LayoutParams lp =
                new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mParallaxImageHeight);
        paddingView.setLayoutParams(lp);
        paddingView.setClickable(true);
        mResultList.addHeaderView(paddingView);
        mListBackgroundView = findViewById(R.id.list_background);
        mLoadingView = (RelativeLayout) findViewById(R.id.rl_loading_view);
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
        onScrollChanged(mResultList.getCurrentScrollY(), false, false);
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
