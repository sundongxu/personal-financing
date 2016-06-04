package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.DeviceInfoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DeviceInfoUtils;

/**
 * Created by roysun on 16/5/5.
 * 设备信息页面
 */
public class DeviceInfoActivity extends BaseActivity {

    public static String TAG = "DeviceInfoActivity";

    private RecyclerView mDeviceInfoList;
    private DeviceInfoListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String cur = DeviceInfoUtils.getMaxCpu(DeviceInfoUtils.CUR);
                    mAdapter.updateInfoValueList(DeviceInfoUtils.changeCpuHZ(cur));
                    mAdapter.notifyDataSetChanged();
                    break;
                case -1:
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.info_device);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDeviceInfoList = (RecyclerView) findViewById(R.id.rv_device_info_list);
        mDeviceInfoList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDeviceInfoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mDeviceInfoList.setItemAnimator(new DefaultItemAnimator());
        mDeviceInfoList.setLayoutManager(mLayoutManager);
        mAdapter = new DeviceInfoListAdapter(this);
        mDeviceInfoList.setAdapter(mAdapter);
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
