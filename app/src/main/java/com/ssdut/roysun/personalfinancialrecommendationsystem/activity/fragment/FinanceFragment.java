package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.StockActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.StockMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FinanceListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FunctionCardListBaseAdapter;


public class FinanceFragment extends BaseFragment {

    public static final String TAG = "FinanceFragment";

    public static FinanceFragment newInstance() {
        FinanceFragment fragment = new FinanceFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
        initCardList(view);
        return view;
    }

    @Override
    public void initCardList(View view) {
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
                        startActivity(new Intent(mContext, StockActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_FORUM:
                        startActivity(new Intent(mContext, StockActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_RECOMMENDATION:
                        startActivity(new Intent(mContext, StockActivity.class));
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void refresh() {
        super.refresh();
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
