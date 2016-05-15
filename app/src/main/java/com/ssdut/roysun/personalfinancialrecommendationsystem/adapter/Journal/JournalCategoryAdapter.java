package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalAddActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.JournalItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.dialog.DialogCategory;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * 记账开销去向、收入来源、借贷类别列表适配器
 */
public class JournalCategoryAdapter extends BaseAdapter {
    //类别列表或类别子类列表的集合
    private ArrayList<String> mCategoryList;
    private Context mContext;
    private int mFlag = 0;
    private String mSubItem = "";
    private RelativeLayout addsub = null;

    public JournalCategoryAdapter(Context context, int flag, String subItem) {
        this.mFlag = flag;
        this.mContext = context;
        //根据传入的参数获取相应的集合
        if (flag == JournalAddActivity.EXPENDITURE) {
            getCategoryList(JournalItem.sExpenseCategory);
        } else if (flag == JournalAddActivity.INCOME) {
            getCategoryList(JournalItem.sIncomeCategory);
        } else if (flag == JournalAddActivity.CREDIT_DEBIT) {
            getCategoryList(JournalItem.sCreditDebitCategory);
        } else if (flag == DialogCategory.FLAG_CATEGORY) {
            getSubCategoryList(subItem);
        }
    }

    /*
     * 获取类别的列表集合
     * */
    public void getCategoryList(String s[]) {
        mCategoryList = new ArrayList<String>();
        for (String str : s) {
            mCategoryList.add(str);
        }
    }

    /*
     * 获取类别子类的列表集合（根据传入的当前选中类别来判断）
     * */
    public void getSubCategoryList(String subitem) {
        mCategoryList = new ArrayList<String>();
        String strs[] = null;
        if (subitem.equals("餐饮")) {
            strs = JournalItem.sMeal;
        } else if (subitem.equals("交通")) {
            strs = JournalItem.sTraffic;
        } else if (subitem.equals("购物")) {
            strs = JournalItem.sShopping;
        } else if (subitem.equals("娱乐")) {
            strs = JournalItem.sEntertainment;
        } else if (subitem.equals("医教")) {
            strs = JournalItem.sMeditationEducation;
        } else if (subitem.equals("居家")) {
            strs = JournalItem.sDailyExpense;
        } else if (subitem.equals("投资")) {
            strs = JournalItem.sInvestment;
        } else if (subitem.equals("人情")) {
            strs = JournalItem.sFavorContact;
        }
        this.mSubItem = subitem;
        for (String str : strs) {
            mCategoryList.add(str);
        }
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mFlag != DialogCategory.FLAG_CATEGORY) {//当标识不为子列表时加载下面的界面
            convertView = LayoutInflater.from(mContext).inflate(R.layout.jz_leibie_item, null);
            String lb = mCategoryList.get(position);
            TextView lb_text = (TextView) convertView.findViewById(R.id.leibie_item_text);
            addsub = (RelativeLayout) convertView.findViewById(R.id.jz_item_addsub_rl);
            convertView.setTag(lb);
            //当前选中标识
            addsub.setTag(lb);
            lb_text.setText(lb);
        } else {//当标识为sub时 加载下面的界面
            convertView = LayoutInflater.from(mContext).inflate(R.layout.jz_leibie_subitem, null);
            TextView sublb_text = (TextView) convertView.findViewById(R.id.leibie_subitem_text);
            String lb = mCategoryList.get(position);
            convertView.setTag(mSubItem + ">" + lb);
            sublb_text.setText(lb);
        }
        return convertView;
    }
}
