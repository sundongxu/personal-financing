package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

/**
 * Created by roysun on 16/4/26.
 * OthersFragment的List适配器
 * 完成list中每一个item的加载 -> 编写item布局
 */
public class OthersListAdapter extends FunctionCardListBaseAdapter {

    public static final String TAG = "FinanceListAdapter";

    public OthersListAdapter(Context context) {
        mContext = context;
        initCardList();
    }

    @Override
    public void initCardList() {
        if (mCardTitleList != null) {
            mCardTitleList.clear();
            mCardTitleList.add("日 记 备 忘");
            mCardTitleList.add("简 易 计 算");
            mCardTitleList.add("彩 云 天 气");
            mCardTitleList.add("中 英 互 译");
        }
        if (mCardDescriptionList != null) {
            mCardDescriptionList.clear();
            mCardDescriptionList.add("生活记录、条目导出、个性设置");
            mCardDescriptionList.add("清爽界面、简单运算、输入友好");
            mCardDescriptionList.add("和风天气、材料设计、地理位置");
            mCardDescriptionList.add("有道翻译、开放平台、网络请求");
        }

        if (mCardIconList != null) {
            mCardIconList.clear();
            mCardIconList.add(R.drawable.icon_tab_4_fun_1_memo);
            mCardIconList.add(R.drawable.icon_tab_4_fun_2_calculator);
            mCardIconList.add(R.drawable.icon_tab_4_fun_3_weather);
            mCardIconList.add(R.drawable.icon_tab_4_fun_4_traslation);
        }

        if (mCardBGList != null) {
            mCardBGList.clear();
            mCardBGList.add(R.drawable.bg_card_3);
            mCardBGList.add(R.drawable.bg_card_12);
            mCardBGList.add(R.drawable.bg_card_16);
            mCardBGList.add(R.drawable.bg_card_10);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                mCardType = CARD_MEMO;
                break;
            case 1:
                mCardType = CARD_CALCULATION;
                break;
            case 2:
                mCardType = CARD_WEATHER;
                break;
            case 3:
                mCardType = CARD_TRANSLATION;
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

//    private AnimatorSet mAnimatorSetOut;
//    private AnimatorSet mAnimatorSetIn;

//    public void setAnimators(final Context context, final View view) {
//        mAnimatorSetIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.anim_in);
//        mAnimatorSetOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.anim_out);
//
//        // 设置点击事件
//        mAnimatorSetIn.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                view.setClickable(false);
//            }
//        });
//
//        mAnimatorSetOut.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                context.startActivity(new Intent(context, TranslationActivity.class));  // finish后会回到onNewIntent
//            }
//        });
//    }

//    private boolean mIsShowBack;

//    public void flipCard(Context context) {
//        FrameLayout cv = (FrameLayout) view;
//        mAnimatorSetIn.setTarget(cv);
//        mAnimatorSetIn.start();
//
//        CardView cvFront = (CardView) ((AppCompatActivity)context).findViewById(R.id.cv_item_front);
//        CardView cvBack = (CardView) ((AppCompatActivity)context).findViewById(R.id.cv_item_back);
//
//        setCameraDistance(context, cvFront, cvBack);
//
//        // 正面朝上
//        if (!mIsShowBack) {
//            mAnimatorSetOut.setTarget(cvFront);
//            mAnimatorSetIn.setTarget(cvBack);
//            mAnimatorSetOut.start();
//            mAnimatorSetIn.start();
//            mIsShowBack = true;
//        } else {
//            // 背面朝上
//            mAnimatorSetOut.setTarget(cvBack);
//            mAnimatorSetIn.setTarget(cvFront);
//            mAnimatorSetOut.start();
//            mAnimatorSetIn.start();
//            mIsShowBack = false;
//        }
//    }

//    private void setCameraDistance(Context context, View view1, View view2) {
//        int distance = 16000;
//        float scale = context.getResources().getDisplayMetrics().density * distance;
//        view1.setCameraDistance(scale);
//        view2.setCameraDistance(scale);
//    }

}
