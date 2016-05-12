package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;

import java.util.ArrayList;

/**
 * Created by roysun on 16/4/26.
 * 搜索框适配器
 * 存在bug：输入字符联想匹配时，还出现删除历史记录条目，待处理
 */
public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mFunList;
    private int mListSize;
    private LayoutInflater mLayoutInflater;
    private boolean mIsFilterList;

    public SearchAdapter(Context context, ArrayList<String> countries, boolean isFilterList) {
        mContext = context;
        mFunList = countries;
        mIsFilterList = isFilterList;
    }


    public void updateList(ArrayList<String> filterList, boolean isFilterList) {
        mFunList = filterList;
        mIsFilterList = isFilterList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFunList.size() > 0) {
            mListSize = mFunList.size() + 1;
            return mListSize;
        } else {
            return mFunList.size();
        }
    }

    @Override
    public String getItem(int position) {
        if (mFunList.size() > 0) {
            if (position < mListSize - 1) {
                return mFunList.get(position);
            } else {
                return "删除历史记录";
            }
        } else {
            return mFunList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            holder = new ViewHolder();
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = mLayoutInflater.inflate(R.layout.search_list_item, parent, false);
            holder.mFunctionText = (TextView) v.findViewById(R.id.txt_country);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Drawable searchDrawable, recentDrawable;

        if (position < mListSize - 1) {
            holder.mFunctionText.setText(mFunList.get(position));
            searchDrawable = mContext.getResources().getDrawable(R.mipmap.icon_search_grey, null);
            recentDrawable = mContext.getResources().getDrawable(R.mipmap.ic_backup_restore_grey600_24dp, null);
        } else {
            holder.mFunctionText.setText("删除历史记录");
            searchDrawable = mContext.getResources().getDrawable(R.mipmap.ic_close_grey600_24dp, null);
            recentDrawable = mContext.getResources().getDrawable(R.mipmap.ic_close_grey600_24dp, null);
        }


        if (mIsFilterList) {
            holder.mFunctionText.setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
        } else {
            holder.mFunctionText.setCompoundDrawablesWithIntrinsicBounds(recentDrawable, null, null, null);
        }
        return v;
    }
}

class ViewHolder {
    TextView mFunctionText;
}

