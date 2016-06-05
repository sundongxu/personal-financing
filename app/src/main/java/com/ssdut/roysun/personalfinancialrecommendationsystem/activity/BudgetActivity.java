package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;

/**
 * Created by roysun on 16/3/12.
 * 预算页面
 */
public class BudgetActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "BudgetActivity";

    private EditText mBudgetAmountView;
    private TextView mBudgetAmountText;
    private Button mBtnSave, mBtnCancel;

    private User mCurUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mCurUser = mUserManager.getCurUser();
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_budget_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBudgetAmountView = (EditText) this.findViewById(R.id.et_budget_amount);
        mBtnSave = (Button) this.findViewById(R.id.btn_budget_save);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) this.findViewById(R.id.btn_budget_cancel);
        mBtnCancel.setOnClickListener(this);
        mBudgetAmountText = (TextView) this.findViewById(R.id.tv_budget_amount);
//        int iBudgetAmountSaved = JournalSqliteHelper.readPreferenceFile(this,
//                JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH);
        int iBudgetAmountSaved = mCurUser.getBudget();
        mBudgetAmountText.setText(iBudgetAmountSaved + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_budget_save:
                if (mBudgetAmountView.getText().toString().length() < 1) {
                    Snackbar.make(mToolbar, R.string.budget_input_not_null, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    return;
                } else if (mBudgetAmountView.getText().toString().length() > 8) {
                    Snackbar.make(mToolbar, R.string.too_much_money, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    return;
                }
                int iBudgetAmountNow = Integer.parseInt(mBudgetAmountView.getText().toString());
//                JournalSqliteHelper.saveBudget(this, JournalSqliteHelper.BUDGET_MONTH,
//                        JournalSqliteHelper.BUDGET_MONTH, iBudgetAmountNow);
                mCurUser.setBudget(iBudgetAmountNow);
                mUserManager.updateUserInfo(mCurUser, mCurUser.getId());
                mCurUser = mUserManager.getCurUser();
                mBudgetAmountText.setText(mCurUser.getBudget() + "");
                Snackbar.make(mToolbar, R.string.budget_save_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                break;
            case R.id.btn_budget_cancel:
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                break;
        }
    }
}
