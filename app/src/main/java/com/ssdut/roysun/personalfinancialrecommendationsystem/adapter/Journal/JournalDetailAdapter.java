package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalDetailActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;

import java.util.ArrayList;

/**
 * Created by roysun on 16/3/12.
 * 记账明细数据Adapter
 */
public class JournalDetailAdapter extends BaseAdapter {

    public static final String TAG = "JournalDetailAdapter";  //类别列表或类别子类列表的集合

    public static ArrayList<?> sDetailList;

    private Context mContext;
    private int mFlag = 0;
    private JournalManager mJournalManager;  //数据库操作

    public JournalDetailAdapter(Context context) {
        mContext = context;
        mJournalManager = JournalManager.getInstance(mContext);
    }

    /**
     * 根据传入的参数获取相应的集合
     *
     * @param year
     * @param month
     * @param day
     * @param flag
     * @return 返回由年月组成的日期内的总支出和总收入两个元素的double数组
     */
    public double[] getList(int year, int month, int day, int flag) {
        mFlag = flag;
        double expenditureSum = 0, incomeSum = 0;

        ArrayList<Expenditure> expenditureList;
        //支出 选择条件
        String expenditureSelection = Expenditure.YEAR + "=" + year + " and " + Expenditure.MONTH + "=" + month;
        expenditureList = mJournalManager.getExpenditureListFromDB(expenditureSelection);
        for (Expenditure _expenditure : expenditureList) {
            expenditureSum += _expenditure.getAmount();
        }

        ArrayList<Income> incomeList;
        //收入 选择条件
        String incomeSelection = Income.YEAR + "=" + year + " and " + Income.MONTH + "=" + month;
        incomeList = mJournalManager.getIncomeListFromDB(incomeSelection);
        for (Income income : incomeList) {
            incomeSum += income.getAmount();
        }
        if (flag == JournalDetailActivity.EXPENDITURE_FLAG) {
            sDetailList = expenditureList;
        } else if (flag == JournalDetailActivity.INCOME_FLAG) {
            sDetailList = incomeList;
        }
        return new double[]{expenditureSum, incomeSum};
    }

    @Override
    public int getCount() {
        return sDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return sDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.journal_detail_list_item, null);
        Expenditure expenditure;
        Income income;
        TextView itemCategory = (TextView) convertView.findViewById(R.id.tv_item_category);
        TextView itemAmount = (TextView) convertView.findViewById(R.id.tv_item_amount);
        TextView itemRemark = (TextView) convertView.findViewById(R.id.tv_item_remark);
        TextView itemTime = (TextView) convertView.findViewById(R.id.tv_item_time);
        if (mFlag == JournalDetailActivity.EXPENDITURE_FLAG) {
            expenditure = (Expenditure) sDetailList.get(position);
            convertView.setTag(expenditure);
            if (expenditure.getSubCategory().length() > 0) {
                itemCategory.setText(expenditure.getCategory() + ">" + expenditure.getSubCategory());
            } else {
                itemCategory.setText(expenditure.getCategory());
            }
            itemAmount.setText(expenditure.getAmount() + "");
            itemRemark.setText(expenditure.getRemark());
            itemTime.setText(expenditure.getDay() + "日 " + expenditure.getTime());
        } else if (mFlag == JournalDetailActivity.INCOME_FLAG) {
            income = (Income) sDetailList.get(position);
            convertView.setTag(income);
            itemCategory.setText(income.getCategory());
            itemAmount.setText(income.getAmount() + "");
            itemRemark.setText(income.getRemark());
            itemTime.setText(income.getDay() + "日 " + income.getTime());
        }
        return convertView;
    }
}
