package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/3/12.
 * 预算页面
 */
public class BudgetActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "BudgetActivity";

    private EditText mBudgetAmount;
    private TextView mHint;
    private Button mBtnSave, mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mBudgetAmount = (EditText) this.findViewById(R.id.et_budget_amount);
        mBtnSave = (Button) this.findViewById(R.id.btn_budget_save);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) this.findViewById(R.id.btn_budget_cancel);
        mBtnCancel.setOnClickListener(this);
        mHint = (TextView) this.findViewById(R.id.tv_budget_hint);
        int _budgetAmountSaved = JournalSqliteHelper.readPreferenceFile(this,
                JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH);
        mHint.setText("本月预算：" + _budgetAmountSaved + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_budget_save:
                if (mBudgetAmount.getText().toString().length() < 1) {
                    ToastUtils.showMsg(this, "输入不能为空");
                    return;
                } else if (mBudgetAmount.getText().toString().length() > 8) {
                    ToastUtils.showMsg(this, "你有这么多钱么？");
                    return;
                }
                int _budgetAmountNow = Integer.parseInt(mBudgetAmount.getText().toString());
                JournalSqliteHelper.saveBudget(this, JournalSqliteHelper.BUDGET_MONTH,
                        JournalSqliteHelper.BUDGET_MONTH, _budgetAmountNow);
                mHint.setText("当前本月预算：" + _budgetAmountNow + "");
                finish();
                break;
            case R.id.btn_budget_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
    }
}
