package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.CalculationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.TranslationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.WeatherActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FunctionCardListBaseAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.OthersListAdapter;

/**
 * Created by roysun on 16/4/26.
 * MD风格的OthersFragment，集成四大功能：
 * （1）备忘录（2）天气预报（3）翻译（4）计算器
 * 做个卡片翻转效果flipcard
 */
public class OthersFragment extends BaseFragment {

    public static final String TAG = "OthersFragment";

    public static OthersFragment newInstance() {
        return new OthersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mContext instanceof MainActivity) {
//            mPreScrollY = ((MainActivity) mContext).getPreScrollYList()[TAB_OTHERS];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, container, false);
        initCardList(view);
        return view;
    }

    @Override
    public void initCardList(View view) {
        super.initCardList(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_others_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());  //线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);
        OthersListAdapter adapter = new OthersListAdapter(mContext);
        adapter.setOnCardClickListener(new FunctionCardListBaseAdapter.OnCardClickListener() {
            @Override
            public void onCardItemClick(int cardType) {
                switch (cardType) {
                    case FunctionCardListBaseAdapter.CARD_MEMO:
                        startActivity(new Intent(mContext, MemoMainActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_CALCULATION:
                        startActivity(new Intent(mContext, CalculationActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_WEATHER:
                        startActivity(new Intent(mContext, WeatherActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_TRANSLATION:
                        startActivity(new Intent(mContext, TranslationActivity.class));
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.smoothScrollBy(0, mPreScrollY);
    }

    @Override
    public void refresh() {
        super.refresh();
//        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onPause() {
//        mCallback.notifyPreScrollY(getScrolledDistance());
        mPreScrollY = getScrolledDistance();
        Log.v(TAG, "滑动量为" + mPreScrollY);
        super.onPause();
    }

}
