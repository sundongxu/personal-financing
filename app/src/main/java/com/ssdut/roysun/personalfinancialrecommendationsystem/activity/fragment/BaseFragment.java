package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by roysun on 16/4/26.
 * MD风格的基础Fragment
 */
public class BaseFragment extends Fragment {

    public static final String TAG = "BaseFragment";

    public static final int TAB_JOURNAL = 0;
    public static final int TAB_FINANCE = 1;
    public static final int TAB_ACCOUNT = 2;
    public static final int TAB_OTHERS = 3;

    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initCardList(View view) {

    }

    public void refresh() {

    }
}
