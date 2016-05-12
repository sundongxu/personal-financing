package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
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
        //传入参数index表明下标，这里下标为3(0、1、2、3)
        OthersFragment fragment = new OthersFragment();
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
