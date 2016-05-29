package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.AssetInfoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;

import java.util.ArrayList;

public class InvestmentSimulationActivity extends BaseActivity {

    public static final String TAG = "InvestmentSimulationActivity";

    private TextView mUserNameText;
    private TextView mBalanceText;
    private TextView mTotalAssetText;
    private RecyclerView mAssetList;
    private AssetInfoListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private StockManager mStockManager;
    private ArrayList<Stock> mStockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_simulation);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mStockManager = StockManager.getInstance(this);
        mStockList = mStockManager.getStockListFromDB("WATCHER_NAME='" + mUserManager.getCurUser().getName() + "'" + " and " + "BUY_NUMBER>0");
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle("模拟投资");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mUserNameText = (TextView) findViewById(R.id.tv_user_name);
        mUserNameText.setText(mUserManager.getCurUser().getName());
        mBalanceText = (TextView) findViewById(R.id.tv_balance);
        mBalanceText.setText(String.format("%.2f", mUserManager.getCurUser().getBalance()));
        mTotalAssetText = (TextView) findViewById(R.id.tv_asset_total);

        mAssetList = (RecyclerView) findViewById(R.id.rv_asset_list);
        mAssetList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAssetList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAssetList.setItemAnimator(new DefaultItemAnimator());
        mAssetList.setLayoutManager(mLayoutManager);
        mAdapter = new AssetInfoListAdapter(this, mStockList);
        mAdapter.setOnItemClickListener(new AssetInfoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Stock stock) {
                if (stock != null) {
                    Intent _intent = new Intent(mContext, StockDetailActivity.class);
                    _intent.putExtra("CODE_SELECTED", stock.getCode());
                    startActivity(_intent);
                }
            }
        });
        mAssetList.setAdapter(mAdapter);
    }

    // 把initView initData放到onResume中会更好，因为每次返回到该Activity时又可以重新加载数据
    @Override
    protected void onResume() {
        super.onResume();
        mBalanceText.setText(String.format("%.2f", mUserManager.getCurUser().getBalance()));
        reLoadAssetList();
    }

    private void reLoadAssetList() {
        mStockList = mStockManager.getStockListFromDB("WATCHER_NAME='" + mUserManager.getCurUser().getName() + "'" + " and " + "BUY_NUMBER>0");
        mAdapter.updateStockList(mStockList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                mInputMethodManager.hideSoftInputFromInputMethod(mToolbar.getWindowToken(), 0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTotalAsset(double totalAssetAmount) {
        mTotalAssetText.setText(String.format("%.2f", totalAssetAmount));
    }
}
