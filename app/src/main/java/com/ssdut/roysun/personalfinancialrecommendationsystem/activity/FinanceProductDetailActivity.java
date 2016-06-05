package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.FinanceProductInfoAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.FinanceProduct;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.FinanceProductManager;

import java.util.ArrayList;

public class FinanceProductDetailActivity extends BaseActivity {

    public static final String TAG = "FinanceProductDetailActivity";

    private Toolbar mToolbar;
    private FloatingActionButton mFabRecommend;
    private RecyclerView mProductInfoList;
    private LinearLayoutManager mLayoutManager;
    private FinanceProductInfoAdapter mAdapter;

    private String mName;
    private ArrayList<String> mProductInfoValueList;
    private FinanceProductManager mProductManager;
    private FinanceProduct mProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_product_detail);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mName = getIntent().getStringExtra("NAME_SELECTED");
        Log.v(TAG, "查询详情的产品名称为" + mName);
        mProductInfoValueList = new ArrayList<>();  // 随时和股票信息列表同步的值List
        mProductManager = FinanceProductManager.getInstance(this);
        mProduct = mProductManager.getProductFromDB(mName);
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.tb_product_detail_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("产品详情");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mProductInfoList = (RecyclerView) findViewById(R.id.rv_product_info_list);
        mProductInfoList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mProductInfoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mProductInfoList.setItemAnimator(new DefaultItemAnimator());
        mProductInfoList.setLayoutManager(mLayoutManager);
        mAdapter = new FinanceProductInfoAdapter(this);
        mProductInfoList.setAdapter(mAdapter);

        mFabRecommend = (FloatingActionButton) findViewById(R.id.fab_recommend);
        mFabRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 购买股票 / 请求K线图
                recommend(mName);
            }
        });
    }

    public void recommend(String productName) {
        if (mUserManager.isSignIn()) {
            Intent _intent = new Intent(this, RecommendationResultActivity.class);
            _intent.putExtra("PRODUCT_NAME", mName);
            startActivity(_intent);
        } else {
            Snackbar.make(mToolbar, "请先登录！", Snackbar.LENGTH_LONG).show();
        }
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
}
