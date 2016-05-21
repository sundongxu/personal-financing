package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/5/9.
 * AccountFragment的List适配器
 * 完成List中每一个item的加载 -> 编写item布局
 */
public class AccountListAdapter extends FunctionCardListBaseAdapter {

    public static final String TAG = "AccountListAdapter";

    public AccountListAdapter(Context context) {
        mContext = context;
        initCardList();
    }

    @Override
    public void initCardList() {
        if (mCardTitleList != null) {
            mCardTitleList.clear();
            mCardTitleList.add("登 录 注 册");
            mCardTitleList.add("个 人 信 息");
            mCardTitleList.add("账 户 管 理");
        }
        if (mCardDescriptionList != null) {
            mCardDescriptionList.clear();
            mCardDescriptionList.add("用户登录、立即注册、密码找回");
            mCardDescriptionList.add("信息修改、资产清单、理财计划");
            mCardDescriptionList.add("账户列表、删除用户、切换当前");
        }

        if (mCardIconList != null) {
            mCardIconList.clear();
            mCardIconList.add(R.drawable.icon_tab_3_fun_1_login_register);
            mCardIconList.add(R.drawable.icon_tab_3_fun_2_information);
            mCardIconList.add(R.drawable.icon_tab_3_fun_3_accounts);
        }

        if (mCardBGList != null) {
            mCardBGList.clear();
            mCardBGList.add(R.drawable.bg_card_1);
            mCardBGList.add(R.drawable.bg_card_13);
            mCardBGList.add(R.drawable.bg_card_4);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                mCardType = CARD_LOGIN_REGISTER;
                break;
            case 1:
                mCardType = CARD_USER_INFO;
                break;
            case 2:
                mCardType = CARD_ACCOUNT_MANAGEMENT;
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
