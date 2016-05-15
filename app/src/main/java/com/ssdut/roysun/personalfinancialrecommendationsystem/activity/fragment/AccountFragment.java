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
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.LoginActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.RegisterActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.UserInfoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.UserManagementActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.AccountListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.functioncard.FunctionCardListBaseAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;


public class AccountFragment extends BaseFragment {

    public static final String TAG = "AccountFragment";

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initCardList(view);
        return view;
    }

    @Override
    public void initCardList(View view) {
        super.initCardList(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_account_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());  //线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);
        AccountListAdapter adapter = new AccountListAdapter(mContext);
        adapter.setOnCardClickListener(new FunctionCardListBaseAdapter.OnCardClickListener() {
            @Override
            public void onCardItemClick(int cardType) {
                switch (cardType) {
                    case FunctionCardListBaseAdapter.CARD_LOGIN_REGISTER:
                        if (mContext instanceof MainActivity) {
                            if (((MainActivity) mContext).getUserManager().isSignIn()) {
                                startActivity(new Intent(mContext, RegisterActivity.class));
                            } else {
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        }
                        break;
                    case FunctionCardListBaseAdapter.CARD_USER_INFO:
                        if (mContext instanceof MainActivity) {
                            if (((MainActivity) mContext).getUserManager().isSignIn()) {
                                startActivity(new Intent(mContext, UserInfoActivity.class));
                            } else {
                                ToastUtils.showMsg(mContext, "你还未登录！");
                            }
                        }
                        break;
                    case FunctionCardListBaseAdapter.CARD_ACCOUNT_MANAGEMENT:
                        if (mContext instanceof MainActivity) {
                            if (((MainActivity) mContext).getUserManager().isSignIn()) {
                                if (((MainActivity) mContext).getUserManager().getCurUser().isSpecial() == 1) {
                                    startActivity(new Intent(mContext, UserManagementActivity.class));
                                } else {
                                    ToastUtils.showMsg(mContext, "你没有管理员权限！");
                                }
                            } else {
                                ToastUtils.showMsg(mContext, "请先登录！");
                            }
                        }
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
