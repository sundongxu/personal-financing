package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.FinanceRecommendationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.InvestmentSimulationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.StockActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.StockMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FinanceListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FunctionCardListBaseAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;


public class FinanceFragment extends BaseFragment {

    public static final String TAG = "FinanceFragment";

    public static FinanceFragment newInstance() {
        return new FinanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mContext instanceof MainActivity) {
//            mPreScrollY = ((MainActivity) mContext).getPreScrollYList()[TAB_FINANCE];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
        initCardList(view);
        return view;
    }

    @Override
    public void initCardList(final View view) {
        super.initCardList(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_finance_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());  //线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);
        FinanceListAdapter adapter = new FinanceListAdapter(mContext);
        adapter.setOnCardClickListener(new FunctionCardListBaseAdapter.OnCardClickListener() {
            @Override
            public void onCardItemClick(int cardType) {
                switch (cardType) {
                    case FunctionCardListBaseAdapter.CARD_STOCKS:
                        startActivity(new Intent(mContext, StockMainActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_INVESTMENT:
                        if (mContext instanceof MainActivity) {
                            if (((MainActivity) mContext).getUserManager().isSignIn()) {
                                startActivity(new Intent(mContext, InvestmentSimulationActivity.class));
                            } else {
                                Snackbar.make(view, R.string.login_first, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            }
                        }
                        break;
                    case FunctionCardListBaseAdapter.CARD_FORUM:
                        startActivity(new Intent(mContext, StockActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_RECOMMENDATION:
                        if (mContext instanceof MainActivity) {
                            if (((MainActivity) mContext).getUserManager().isSignIn()) {
                                startActivity(new Intent(mContext, FinanceRecommendationActivity.class));
                            } else {
                                Snackbar.make(view, R.string.login_first, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                            }
                        }
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.smoothScrollBy(0, mPreScrollY);
//        mRecyclerView.scrollBy(0,mPreScrollY);
    }

    @Override
    public void refresh() {
        super.refresh();
//        mRecyclerView.smoothScrollToPosition(50);
    }

    @Override
    public void onPause() {
//        mCallback.notifyPreScrollY(getScrolledDistance());
        mPreScrollY = getScrolledDistance();
        Log.v(TAG, "滑动量为" + mPreScrollY);
        super.onPause();
    }
}
