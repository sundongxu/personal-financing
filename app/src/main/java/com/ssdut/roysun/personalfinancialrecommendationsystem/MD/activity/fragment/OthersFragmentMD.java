package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter.OthersListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/4/26.
 * MD风格的OthersFragment，集成四大功能：
 * （1）备忘录（2）天气预报（3）翻译（4）计算器
 * 做个卡片翻转效果flipcard
 */
public class OthersFragmentMD extends BaseFragmentMD {

    public static final String TAG = "OthersFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public static OthersFragmentMD newInstance(int index) {
        //传入参数index表明下标，这里下标为3(0、1、2、3)
        OthersFragmentMD fragment = new OthersFragmentMD();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (getArguments().getInt("index", -1) == 3) {
            View view = inflater.inflate(R.layout.fragment_others_md, container, false);
            initOthersList(view);
            return view;
//        } else {
//            return null;
//        }
    }

    private void initOthersList(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_others_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());  //线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            itemsData.add("Fragment " + getArguments().getInt("index", -1) + " / Item " + i);
        }

        OthersListAdapter adapter = new OthersListAdapter(itemsData);
        mRecyclerView.setAdapter(adapter);
    }

    public void refresh() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
