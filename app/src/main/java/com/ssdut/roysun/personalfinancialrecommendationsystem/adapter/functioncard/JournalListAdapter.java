package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/5/9.
 * JournalListFragment的List适配器
 * 完成list中每一个item的加载 -> 编写item布局
 */
public class JournalListAdapter extends FunctionCardListBaseAdapter {

    public static final String TAG = "JournalListAdapter";

    public JournalListAdapter(Context context) {
        mContext = context;
        initCardList();
    }

    @Override
    public void initCardList() {
        if (mCardTitleList != null) {
            mCardTitleList.clear();
            mCardTitleList.add("目 标 预 算");
            mCardTitleList.add("账 目 明 细");
            mCardTitleList.add("统 计 图 表");
            mCardTitleList.add("消 费 推 荐");
            mCardTitleList.add("记 账 设 置");
        }
        if (mCardDescriptionList != null) {
            mCardDescriptionList.clear();
            mCardDescriptionList.add("盈利预期、消费计划、预算更新");
            mCardDescriptionList.add("收入来源、开销去向、生活记录");
            mCardDescriptionList.add("收益趋势、支出折线、预算使用");
            mCardDescriptionList.add("旅游规划、美味不等、潮流达人");
            mCardDescriptionList.add("跨度调整、记录清空、记账加密");
        }

        if (mCardIconList != null) {
            mCardIconList.clear();
            mCardIconList.add(R.drawable.icon_tab_1_fun_1_budget);
            mCardIconList.add(R.drawable.icon_tab_1_fun_2_journal);
            mCardIconList.add(R.drawable.icon_tab_1_fun_3_graphics);
            mCardIconList.add(R.drawable.icon_tab_1_fun_4_consumption);
            mCardIconList.add(R.drawable.icon_tab_1_fun_5_settings);
        }

        if (mCardBGList != null) {
            mCardBGList.clear();
            mCardBGList.add(R.drawable.bg_card_17);
            mCardBGList.add(R.drawable.bg_card_9);
            mCardBGList.add(R.drawable.bg_card_15);
            mCardBGList.add(R.drawable.bg_card_2);
            mCardBGList.add(R.drawable.bg_card_6);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                mCardType = CARD_BUDGET;
                break;
            case 1:
                mCardType = CARD_JOURNAL;
                break;
            case 2:
                mCardType = CARD_GRAPHICS;
                break;
            case 3:
                mCardType = CARD_CONSUMATION;
                break;
            case 4:
                mCardType = CARD_SETTINGS;
                break;
        }
        return mCardType;
    }

    @Override
    public FunctionCardListBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.function_card_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardItemClick(viewType);
            }
        });
        return new FunctionCardListBaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FunctionCardListBaseAdapter.ViewHolder holder, int position) {
        holder.mCardTitleText.setText(mCardTitleList.get(position));
        holder.mCardDescriptionText.setText(mCardDescriptionList.get(position));
        holder.mCardIcon.setImageResource(mCardIconList.get(position));
        holder.mCardBackground.setBackgroundResource(mCardBGList.get(position));
    }
}
