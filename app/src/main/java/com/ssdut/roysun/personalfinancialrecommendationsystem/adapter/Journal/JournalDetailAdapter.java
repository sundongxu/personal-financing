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
        this.mContext = context;
        mJournalManager = new JournalManager(context);
    }

    /**
     * 根据传入的参数获取相应的集合
     * @param year
     * @param month
     * @param day
     * @param flag
     * @return 返回由年月组成的日期内的总支出和总收入两个元素的double数组
     */
    public double[] getList(int year, int month, int day, int flag) {
        this.mFlag = flag;
        double _expenditureSum = 0, _incomeSum = 0;

        ArrayList<Expenditure> _expenditureList;
        //支出 选择条件
        String _expenditureSelection = Expenditure.YEAR + "=" + year + " and " + Expenditure.MONTH + "=" + month;
        _expenditureList = mJournalManager.getExpenditureListFromDB(_expenditureSelection);
        for (Expenditure _expenditure : _expenditureList) {
            _expenditureSum += _expenditure.getAmount();
        }

        ArrayList<Income> _incomeList;
        //收入 选择条件
        String _incomeSelection = Income.YEAR + "=" + year + " and " + Income.MONTH + "=" + month;
        _incomeList = mJournalManager.getIncomeListFromDB(_incomeSelection);
        for (Income _income : _incomeList) {
            _incomeSum += _income.getAmount();
        }
        if (flag == JournalDetailActivity.EXPENDITURE_FLAG) {
            sDetailList = _expenditureList;
        } else if (flag == JournalDetailActivity.INCOME_FLAG) {
            sDetailList = _incomeList;
        }
        return new double[]{_expenditureSum, _incomeSum};
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
        Expenditure _expenditure;
        Income _income;
        TextView _itemCategory = (TextView) convertView.findViewById(R.id.tv_item_category);
        TextView _itemAmount = (TextView) convertView.findViewById(R.id.tv_item_amount);
        TextView _itemRemark = (TextView) convertView.findViewById(R.id.tv_item_remark);
        TextView _itemTime = (TextView) convertView.findViewById(R.id.tv_item_time);
        if (mFlag == JournalDetailActivity.EXPENDITURE_FLAG) {
            _expenditure = (Expenditure) sDetailList.get(position);
            convertView.setTag(_expenditure);
            if (_expenditure.getSubCategory().length() > 0) {
                _itemCategory.setText(_expenditure.getCategory() + ">" + _expenditure.getSubCategory());
            } else {
                _itemCategory.setText(_expenditure.getCategory());
            }
            _itemAmount.setText(_expenditure.getAmount() + "");
            _itemRemark.setText(_expenditure.getRemark());
            _itemTime.setText(_expenditure.getDay() + "日 " + _expenditure.getTime());
        } else if (mFlag == JournalDetailActivity.INCOME_FLAG) {
            _income = (Income) sDetailList.get(position);
            convertView.setTag(_income);
            _itemCategory.setText(_income.getCategory());
            _itemAmount.setText(_income.getAmount() + "");
            _itemRemark.setText(_income.getRemark());
            _itemTime.setText(_income.getDay() + "日 " + _income.getTime());
        }
        return convertView;
    }
}
