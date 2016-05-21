package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/15.
 * RecyclerView + ViewHolder + Adapter 测试，成功！ toolbar成功隐藏！
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private ArrayList<String> mTitle = new ArrayList<>();
    private Context mContext;

    public TestAdapter(Context context) {
        mContext = context;
        initTitleList();
    }

    public void initTitleList() {
        if (mTitle != null) {
            mTitle.clear();
            for (int i = 0; i < 20; i++) {
                mTitle.add("哈哈哈哈");
            }
        }
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        TestViewHolder viewHolder = new TestViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.mTextView.setText(mTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public TestViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_test);
        }
    }
}
