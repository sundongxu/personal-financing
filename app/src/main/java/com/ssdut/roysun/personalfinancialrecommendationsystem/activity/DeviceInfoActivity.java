package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/5/5.
 * 设备信息界面
 */
public class DeviceInfoActivity extends BaseActivity {

    public static String TAG = "DeviceInfoActivity";

    private Toolbar mToolbar;
    private RecyclerView mInfoList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.info_device);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mInfoList = (RecyclerView) findViewById(R.id.rv_device_info_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
