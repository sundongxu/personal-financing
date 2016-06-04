package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by roysun on 16/5/29.
 * 应用信息List适配器
 */
public class AppInfoListAdapter extends InfoListBaseAdapter {

    public static final String TAG = "AppInfoListAdapter";

    private OnItemClickListener mListener;

    public AppInfoListAdapter(Context context) {
        mContext = context;
        initInfoList();
    }

    @Override
    public void initInfoList() {
        if (mInfoTitleList != null) {
            mInfoTitleList.clear();
            mInfoTitleList.add("随心记");
            mInfoTitleList.add("当前版本");
            mInfoTitleList.add("推荐给朋友");
            mInfoTitleList.add("点赞");
            mInfoTitleList.add("打赏");
            mInfoTitleList.add("关于作者");
            mInfoTitleList.add("个人博客");
            mInfoTitleList.add("GitHub");
            mInfoTitleList.add("邮箱");
            mInfoTitleList.add("版权");
            mInfoTitleList.add("股票数据来源");
            mInfoTitleList.add("天气数据来源");
            mInfoTitleList.add("翻译数据来源");
            mInfoTitleList.add("素材来源");
        }
        if (mInfoValueList != null) {
            mInfoValueList.clear();
            mInfoValueList.add("应用介绍");
            mInfoValueList.add("1.0.0");
            mInfoValueList.add("分享 --- 随心记");
            mInfoValueList.add("给个Star，鼓励作者(๑¯◡¯๑)");
            mInfoValueList.add("请我喝杯咖啡？(〃ω〃)");
            mInfoValueList.add("孙东旭   大连理工大学   软件学院");
            mInfoValueList.add("http://blog.csdn.net/u011348727");
            mInfoValueList.add("https://github.com/sdx18973089012");
            mInfoValueList.add("371211947@qq.com");
            mInfoValueList.add("毕设项目");
            mInfoValueList.add("新浪、Yahoo股票API");
            mInfoValueList.add("和风天气预报API");
            mInfoValueList.add("有道翻译API");
            mInfoValueList.add("图片素材来源于网络，版权属于原作者");
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                mItemType = INFO_APP_INTRODUCTION;
                break;
            case 1:
                mItemType = INFO_APP_VERSION;
                break;
            case 2:
                mItemType = INFO_APP_RECOMMENDATION;
                break;
            case 3:
                mItemType = INFO_APP_STAR;
                break;
            case 4:
                mItemType = INFO_APP_ENCOURAGE;
                break;
            case 5:
                mItemType = INFO_APP_AUTHOR;
                break;
            case 6:
                mItemType = INFO_APP_BLOG;
                break;
            case 7:
                mItemType = INFO_APP_GITHUB;
                break;
            case 8:
                mItemType = INFO_APP_EMAIL;
                break;
            case 9:
                mItemType = INFO_APP_COPYRIGHT;
                break;
            case 10:
                mItemType = INFO_APP_STOCK_SOURCE;
                break;
            case 11:
                mItemType = INFO_APP_WEATHER_SOURCE;
                break;
            case 12:
                mItemType = INFO_APP_TRANSLATION_SOURCE;
                break;
            case 13:
                mItemType = INFO_APP_PIC_SOURCE;
                break;
        }
        return mItemType;
    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(InfoItemViewHolder holder, final int position) {
        final int _itemType = getItemViewType(position);
        holder.mItemTitleText.setText(mInfoTitleList.get(position));
        holder.mItemValueText.setText(mInfoValueList.get(position));
        holder.mItemArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(_itemType);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int itemType);
    }
}
