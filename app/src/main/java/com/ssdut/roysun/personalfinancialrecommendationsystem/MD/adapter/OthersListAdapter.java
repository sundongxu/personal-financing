package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.LoginActivityMD;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/4/26.
 * OthersFragment的list适配器
 * 完成list中每一个item的加载 -> 编写item布局
 */
public class OthersListAdapter extends RecyclerView.Adapter<OthersListAdapter.ViewHolder> {

    private ArrayList<String> mDataset = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_item_title_back);
        }
    }

    public OthersListAdapter(ArrayList<String> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
    }

    @Override
    public OthersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_fragment_list_item, parent, false);
//        setAnimators(v.getContext(), v);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flipCard(v.getContext());
//            }
//        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), LoginActivityMD.class));
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OthersListAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private AnimatorSet mAnimatorSetOut;
    private AnimatorSet mAnimatorSetIn;

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

    private boolean mIsShowBack;

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

    private void setCameraDistance(Context context, View view1, View view2) {
        int distance = 16000;
        float scale = context.getResources().getDisplayMetrics().density * distance;
        view1.setCameraDistance(scale);
        view2.setCameraDistance(scale);
    }

}
