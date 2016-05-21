package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/5/9.
 * FinanceFragment的List适配器
 * 完成list中每一个item的加载 -> 编写item布局
 */
public class FinanceListAdapter extends FunctionCardListBaseAdapter {

    public static final String TAG = "FinanceListAdapter";

    public FinanceListAdapter(Context context) {
        mContext = context;
        initCardList();
    }

    @Override
    public void initCardList() {
        if (mCardTitleList != null) {
            mCardTitleList.clear();
            mCardTitleList.add("股 票 走 势");
            mCardTitleList.add("模 拟 投 资");
            mCardTitleList.add("金 融 论 坛");
            mCardTitleList.add("理 财 建 议");
        }
        if (mCardDescriptionList != null) {
            mCardDescriptionList.clear();
            mCardDescriptionList.add("实时看盘、重点关注、证券买卖");
            mCardDescriptionList.add("账户盈余、股票期权、基金投入");
            mCardDescriptionList.add("社区精华、产品观察、机遇发现");
            mCardDescriptionList.add("智能推荐、指标筛选、个性定制");
        }

        if (mCardIconList != null) {
            mCardIconList.clear();
            mCardIconList.add(R.drawable.icon_tab_2_fun_1_stocks);
            mCardIconList.add(R.drawable.icon_tab_2_fun_2_investment);
            mCardIconList.add(R.drawable.icon_tab_2_fun_3_forum);
            mCardIconList.add(R.drawable.icon_tab_2_fun_4_recommendation);
        }

        if (mCardBGList != null) {
            mCardBGList.clear();
            mCardBGList.add(R.drawable.bg_card_8);
            mCardBGList.add(R.drawable.bg_card_11);
            mCardBGList.add(R.drawable.bg_card_5);
            mCardBGList.add(R.drawable.bg_card_14);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                mCardType = CARD_STOCKS;
                break;
            case 1:
                mCardType = CARD_INVESTMENT;
                break;
            case 2:
                mCardType = CARD_FORUM;
                break;
            case 3:
                mCardType = CARD_RECOMMENDATION;
                break;
        }
        return mCardType;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
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
