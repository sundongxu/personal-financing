package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/11.
 * 主界面功能卡片列表的适配器基类，所有Tab的功能卡片列表适配器都要继承它
 */
public class FunctionCardListBaseAdapter extends RecyclerView.Adapter<FunctionCardListBaseAdapter.ViewHolder> {

    public static final String TAG = "FunctionCardListBaseAdapter";

    // Tab 1
    public static final int CARD_BUDGET = 9011;
    public static final int CARD_JOURNAL = 9012;
    public static final int CARD_GRAPHICS = 9013;
    public static final int CARD_CONSUMATION = 9014;
    public static final int CARD_SETTINGS = 9015;

    // Tab 2
    public static final int CARD_STOCKS = 9021;
    public static final int CARD_INVESTMENT = 9022;
    public static final int CARD_FORUM = 9023;
    public static final int CARD_RECOMMENDATION = 9024;

    // Tab 3
    public static final int CARD_LOGIN_REGISTER = 9031;
    public static final int CARD_USER_INFO = 9032;
    public static final int CARD_ACCOUNT_MANAGEMENT = 9033;

    // Tab 4
    public static final int CARD_MEMO = 9041;
    public static final int CARD_CALCULATION = 9042;
    public static final int CARD_WEATHER = 9043;
    public static final int CARD_TRANSLATION = 9044;

    public ArrayList<String> mCardTitleList = new ArrayList<>();
    public ArrayList<String> mCardDescriptionList = new ArrayList<>();
    public ArrayList<Integer> mCardIconList = new ArrayList<>();
    public ArrayList<Integer> mCardBGList = new ArrayList<>();

    public int mCardType;
    public Context mContext;
    public OnCardClickListener mListener;

    public void initCardList() {

    }

    @Override
    public int getItemCount() {
        return mCardTitleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public FunctionCardListBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FunctionCardListBaseAdapter.ViewHolder holder, int position) {

    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        mListener = listener;
    }

    public interface OnCardClickListener {
        void onCardItemClick(int cardType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // 卡片视图内容
        public TextView mCardTitleText;  // 标题
        public TextView mCardDescriptionText;  // 功能描述
        public ImageView mCardIcon;  // 图表，见图知意
        public ImageView mCardBackground;  // 背景图

        public ViewHolder(View v) {
            super(v);
            mCardTitleText = (TextView) v.findViewById(R.id.tv_item_title_front);
            mCardDescriptionText = (TextView) v.findViewById(R.id.tv_item_description_front);
            mCardIcon = (ImageView) v.findViewById(R.id.iv_item_fun_icon);
            mCardBackground = (ImageView) v.findViewById(R.id.iv_item_bg_front);
        }
    }
}
