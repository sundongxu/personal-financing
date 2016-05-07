package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.MemoContent;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.MemoManager;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * 备忘录ListView的适配器
 */
public class MemoListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    public static ArrayList<MemoContent> sMemoList;  //类别列表或类别子类列表的集合
    public ArrayList<Integer> mIdList;  //存储多选item的id
    public boolean isShowCheck;  //是否显示多选框
    public boolean isSelectAll;  //是否全选
    private Context mContext;

    public MemoListAdapter(Context context) {
        this.mContext = context;
        sMemoList = new ArrayList<MemoContent>();
        mIdList = new ArrayList<Integer>();
        initList();
        isShowCheck = false;
        isSelectAll = false;
    }

    /**
     * 从数据库中取出备忘录列表数据条目填充adapter
     */
    public void initList() {
        MemoManager _memoManager = new MemoManager(mContext);
        sMemoList = _memoManager.getMemoListFromDB("");
    }

    @Override
    public int getCount() {
        return sMemoList.size();
    }

    @Override
    public Object getItem(int position) {
        return sMemoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("sdx---getView invoked!!!");
        convertView = LayoutInflater.from(mContext).inflate(R.layout.memo_list_item, null);
        MemoContent _memoContent;
        _memoContent = sMemoList.get(position);
        convertView.setTag(_memoContent.getId());
        TextView content = (TextView) convertView.findViewById(R.id.tv_memo_list_item_content);
        TextView date = (TextView) convertView.findViewById(R.id.tv_memo_list_item_date);
        TextView week = (TextView) convertView.findViewById(R.id.tv_memo_list_item_week);
        TextView time = (TextView) convertView.findViewById(R.id.tv_memo_list_item_time);
        content.setText(_memoContent.getContent());
        content.setSingleLine(true);
        date.setText(_memoContent.getMonth() + "月" + _memoContent.getDay() + "日");
        time.setText(_memoContent.getTime());
        String weekString = "星期";
        if (_memoContent.getWeek() == 1) {
            weekString = weekString + "一";
        } else if (_memoContent.getWeek() == 2) {
            weekString = weekString + "二";
        } else if (_memoContent.getWeek() == 3) {
            weekString = weekString + "三";
        } else if (_memoContent.getWeek() == 4) {
            weekString = weekString + "四";
        } else if (_memoContent.getWeek() == 5) {
            weekString = weekString + "五";
        } else if (_memoContent.getWeek() == 6) {
            weekString = weekString + "六";
        } else if (_memoContent.getWeek() == 7) {
            weekString = weekString + "日";
        }
        week.setText(weekString);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb_memo_list_item);
        cb.setTag(_memoContent.getId());
        cb.setOnCheckedChangeListener(this);
        if (!isShowCheck) {
            cb.setVisibility(View.GONE);
        } else {
            cb.setVisibility(View.VISIBLE);
            if (isSelectAll) {
                cb.setChecked(true);
            } else {
                mIdList.clear();
                cb.setChecked(false);
            }
        }
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            System.out.println("add(Integer)v.getTag())==" + buttonView.getTag());
            mIdList.add((Integer) buttonView.getTag());
        } else {
            System.out.println("remove(Integer)v.getTag())==" + buttonView.getTag());
            mIdList.remove((Integer) buttonView.getTag());
        }
    }
}
