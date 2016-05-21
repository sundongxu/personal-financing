package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/20.
 * 股票详情页面适配器
 */
public class StockInfoAdapter extends InfoListBaseAdapter {

    public static final String TAG = "StockInfoAdapter";

    public StockInfoAdapter(Context context) {
        mContext = context;
        initInfoList();
    }

    @Override
    public void initInfoList() {
//        super.initInfoList();
        if (mInfoTitleList != null) {
            mInfoTitleList.clear();
            // 35个指标
            mInfoTitleList.add("股票代码：");
            mInfoTitleList.add("股票名称：");
            mInfoTitleList.add("当前价格：");
            mInfoTitleList.add("涨跌幅：");
            mInfoTitleList.add("涨跌额：");
            mInfoTitleList.add("当前日期：");
            mInfoTitleList.add("更新时间：");
            mInfoTitleList.add("今日开盘价：");
            mInfoTitleList.add("昨日收盘价：");
            mInfoTitleList.add("今日最高价：");
            mInfoTitleList.add("今日最低价：");
            mInfoTitleList.add("竞买价：");
            mInfoTitleList.add("竞卖价：");
            mInfoTitleList.add("成交量：");
            mInfoTitleList.add("成交价：");
            mInfoTitleList.add("买一：");
            mInfoTitleList.add("买一价：");
            mInfoTitleList.add("买二：");
            mInfoTitleList.add("买二价：");
            mInfoTitleList.add("买三：");
            mInfoTitleList.add("买三价：");
            mInfoTitleList.add("买四：");
            mInfoTitleList.add("买四价：");
            mInfoTitleList.add("买五：");
            mInfoTitleList.add("买五价：");
            mInfoTitleList.add("卖一：");
            mInfoTitleList.add("卖一价：");
            mInfoTitleList.add("卖二：");
            mInfoTitleList.add("卖二价：");
            mInfoTitleList.add("卖三：");
            mInfoTitleList.add("卖三价：");
            mInfoTitleList.add("卖四：");
            mInfoTitleList.add("卖四价：");
            mInfoTitleList.add("卖五：");
            mInfoTitleList.add("卖五价：");
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

    // 更新完值List再notifyDataSetChanged通知数据改变重新绘制List
    public void updateStockInfoValueList(ArrayList<String> infoValueList) {
        if (mInfoTitleList.size() == infoValueList.size()) {
            mInfoValueList = infoValueList;
        } else {
            Log.v(TAG, "股票信息列表条目数目不匹配！");
        }
    }

    @Override
    public void onBindViewHolder(InfoItemViewHolder holder, int position) {
        holder.mItemIcon.setVisibility(View.GONE);
        int _textColor = mContext.getResources().getColor(R.color.black);
        if (mInfoValueList.get(3).contains("+") && (position == 2 || position == 3 || position == 4)) {
            _textColor = mContext.getResources().getColor(R.color.red);
        } else if (mInfoValueList.get(3).contains("+") && (position == 2 || position == 3 || position == 4)) {
            _textColor = mContext.getResources().getColor(R.color.green);
        }
        holder.mItemTitleText.setText(mInfoTitleList.get(position));
//        holder.mItemTitleText.setTextColor(_textColor);
        holder.mItemValueText.setText(mInfoValueList.get(position));
        holder.mItemValueText.setTextColor(_textColor);
    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
