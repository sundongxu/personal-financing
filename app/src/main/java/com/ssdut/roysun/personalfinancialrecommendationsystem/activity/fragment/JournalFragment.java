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
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.BudgetActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.ForumWebActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalSettingActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalSheetActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FunctionCardListBaseAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.JournalListAdapter;


public class JournalFragment extends BaseFragment {

    public static final String TAG = "JournalFragment";

    public static JournalFragment newInstance() {
        return new JournalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mContext instanceof MainActivity) {
//            mPreScrollY = ((MainActivity) mContext).getPreScrollYList()[TAB_JOURNAL];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);
        initCardList(view);
        return view;
    }

    @Override
    public void initCardList(View view) {
        super.initCardList(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_journal_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());  //线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);
        JournalListAdapter adapter = new JournalListAdapter(mContext);
        adapter.setOnCardClickListener(new FunctionCardListBaseAdapter.OnCardClickListener() {
            @Override
            public void onCardItemClick(int cardType) {
                switch (cardType) {
                    case FunctionCardListBaseAdapter.CARD_BUDGET:
                        startActivity(new Intent(mContext, BudgetActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_JOURNAL:
                        startActivity(new Intent(mContext, JournalMainActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_GRAPHICS:
                        startActivity(new Intent(mContext, JournalSheetActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_CONSUMATION:
//                        startActivity(new Intent(mContext, JournalMainActivity.class));
                        startActivity(new Intent(mContext, ForumWebActivity.class));
                        break;
                    case FunctionCardListBaseAdapter.CARD_SETTINGS:
                        startActivity(new Intent(mContext, JournalSettingActivity.class));
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
