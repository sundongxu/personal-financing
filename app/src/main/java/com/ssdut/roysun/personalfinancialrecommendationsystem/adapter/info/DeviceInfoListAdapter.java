package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;

import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DeviceInfoUtils;

/**
 * Created by roysun on 16/5/29.
 * 设备信息List适配器
 */
public class DeviceInfoListAdapter extends InfoListBaseAdapter {

    public static final String TAG = "DeviceInfoListAdapter";

    public DeviceInfoListAdapter(Context context) {
        mContext = context;
        initInfoList();
    }

    @Override
    public void initInfoList() {
        if (mInfoTitleList != null) {
            mInfoTitleList.clear();
            mInfoTitleList.add("手机型号");
            mInfoTitleList.add("系统版本");
            mInfoTitleList.add("安卓版本");
            mInfoTitleList.add("芯片型号");
            mInfoTitleList.add("最大频率");
            mInfoTitleList.add("当前频率");
            mInfoTitleList.add("SD卡容量  剩余");
            mInfoTitleList.add("ROM容量  剩余");
            mInfoTitleList.add("RAM容量  剩余");
            mInfoTitleList.add("开机时长");
        }
        if (mInfoValueList != null) {
            String[] _versions = DeviceInfoUtils.getVersion();
            mInfoValueList.add(_versions[2]);
            mInfoValueList.add(_versions[3]);
            mInfoValueList.add(_versions[1]);
            mInfoValueList.add(DeviceInfoUtils.getCpuName());
            String _max = DeviceInfoUtils.getMaxCpu(DeviceInfoUtils.MAX);
            mInfoValueList.add(DeviceInfoUtils.changeCpuHZ(_max));
            String _cur = DeviceInfoUtils.getMaxCpu(DeviceInfoUtils.CUR);
            mInfoValueList.add(DeviceInfoUtils.changeCpuHZ(_cur));
            String[] _sds = DeviceInfoUtils.getSDCardMemory();
            mInfoValueList.add(_sds[0] + "GB  " + _sds[1] + "GB");
            String[] _roms = DeviceInfoUtils.getRomMemroy();
            mInfoValueList.add(_roms[0] + "GB  " + _roms[1] + "GB");
            String[] _rams = DeviceInfoUtils.getTotalMemory();
            if (_rams != null) {
                mInfoValueList.add(_rams[0] + "MB  " + _rams[1] + "MB");
            }
            mInfoValueList.add(DeviceInfoUtils.getTimes());
        }
    }

    public void updateInfoValueList(String curCpuHZ) {
        mInfoValueList.set(5, curCpuHZ);
    }

    @Override
    public void onBindViewHolder(InfoItemViewHolder holder, int position) {
        holder.mItemTitleText.setText(mInfoTitleList.get(position));
        holder.mItemValueText.setText(mInfoValueList.get(position));
    }
}
