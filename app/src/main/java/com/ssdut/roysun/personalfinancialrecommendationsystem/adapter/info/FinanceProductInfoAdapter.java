package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by roysun on 16/5/30.
 * 理财产品详情页适配器
 */
public class FinanceProductInfoAdapter extends InfoListBaseAdapter {

    public static final String TAG = "StockInfoAdapter";

    public FinanceProductInfoAdapter(Context context) {
        mContext = context;
        initInfoList();
    }

    @Override
    public void initInfoList() {
//        super.initInfoList();
        if (mInfoTitleList != null) {
            mInfoTitleList.clear();
            mInfoTitleList.add("产品名称");
            mInfoTitleList.add("产品类型");
            mInfoTitleList.add("收益率");
            mInfoTitleList.add("投资周期");
            mInfoTitleList.add("起购金额");
            mInfoTitleList.add("手续费率");
            mInfoTitleList.add("同类产品");
        }

        if (mInfoValueList != null) {
            mInfoValueList.clear();
            for (int i = 0; i < mInfoTitleList.size(); i++) {
                mInfoValueList.add("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(InfoItemViewHolder holder, int position) {
        holder.mItemIcon.setVisibility(View.GONE);
        holder.mItemTitleText.setText(mInfoTitleList.get(position));
        holder.mItemValueText.setText(mInfoValueList.get(position));
    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
