package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;

/**
 * Created by roysun on 16/3/12.
 * 记账设置页面
 */
public class JournalSettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalSettingActivity";

    public static final String JOURNAL_PASSWORD = "JOURNAL_PASSWORD";

    private EditText mPsdInputView, mPsdRepeatView;  // 密码编辑框
    private TextView mTitleText;  //设置密码标题
    private Button mBtnSave, mBtnCancel, mBtnDelete;  // 密码编辑界面的保存，取消，删除按钮
    private RelativeLayout mPsdTitleArea, mClearDataArea;  // 设置选项  加密，清除数据，关于软件
    private LinearLayout mPsdSetArea;  //编辑加密界面
    private int mPsd = 0, mPsdTemp = -1;  // mPsd 当前密码,temp密码标识

    private JournalManager mJournalManager;  //记账数据库管理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_setting);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mJournalManager = JournalManager.getInstance(this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_journal_setting_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mPsdTitleArea = (RelativeLayout) this.findViewById(R.id.rl_psd_title);
        mPsdTitleArea.setOnClickListener(this);
        mClearDataArea = (RelativeLayout) this.findViewById(R.id.rl_clear_all);
        mClearDataArea.setOnClickListener(this);
        mPsdSetArea = (LinearLayout) this.findViewById(R.id.ll_psd_set_area);
        mPsdSetArea.setVisibility(View.GONE);

        mPsdInputView = (EditText) this.findViewById(R.id.et_new_psd);
        mPsdRepeatView = (EditText) this.findViewById(R.id.et_psd_repeat);
        mBtnSave = (Button) this.findViewById(R.id.btn_psd_confirm);
        mBtnSave.setOnClickListener(this);
        mBtnCancel = (Button) this.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mBtnDelete = (Button) this.findViewById(R.id.btn_psd_delete);
        mBtnDelete.setOnClickListener(this);
        mBtnDelete.setVisibility(View.INVISIBLE);

        mTitleText = (TextView) this.findViewById(R.id.tv_psd_title);
        mPsd = JournalSqliteHelper.readPreferenceFile(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD);
        if (mPsd == 0) {
            // 密码为0则当前没密码
            mTitleText.setText(R.string.journal_setting_encrypt);
        } else {
            mTitleText.setText(R.string.journal_setting_create_update_password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_psd_title:
                //程序加密选项，打开后再点击可收回（抽屉式）
                mPsd = JournalSqliteHelper.readPreferenceFile(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD);
                if (mPsdSetArea.isShown()) {
                    mPsdSetArea.setVisibility(View.GONE);
                } else {
                    mPsdSetArea.setVisibility(View.VISIBLE);
                    if (mPsd == 0) {
                        //当没有密码时
                        mTitleText.setText(R.string.journal_setting_encrypt);
                        mBtnDelete.setVisibility(View.INVISIBLE);
                    } else {
                        mPsdTemp = mPsd;
                        //当有密码时把密码赋值给temp，以便于点击确定按钮时正确判断
                        mTitleText.setText(R.string.journal_setting_create_update_password);
                        mPsdInputView.setHint(R.string.journal_setting_input_old_password);
                        mPsdRepeatView.setVisibility(View.GONE);  //密码确认框不可见
                        mBtnDelete.setVisibility(View.INVISIBLE);  //删除密码按钮不可见
                    }
                }
                break;
            case R.id.rl_clear_all:
                showDialog();
                break;
            case R.id.btn_psd_confirm:
                if (mPsd == 0 || mPsdTemp == -1) {
                    //当没有密码时
                    if (mPsdInputView.getText().toString().length() == 0 || mPsdRepeatView.getText().toString().length() == 0) {
                        Snackbar.make(mToolbar, R.string.journal_setting_input_not_null, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        return;
                    }
                    if (mPsdInputView.getText().toString().length() != 6 || mPsdRepeatView.getText().toString().length() != 6) {
                        Snackbar.make(mToolbar, R.string.journal_setting_input_6_digit_password, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        return;
                    }
                    if (!mPsdInputView.getText().toString().equals(mPsdRepeatView.getText().toString())) {
                        Snackbar.make(mToolbar, R.string.journal_setting_password_repeat_not_match, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        return;
                    }
                    mPsd = Integer.parseInt(mPsdRepeatView.getText().toString());
                    mPsdTemp = mPsd;
                    JournalSqliteHelper.saveBudget(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD, mPsd);
                    mTitleText.setText(R.string.journal_setting_create_update_password);
                    mPsdSetArea.setVisibility(View.GONE);
                    Snackbar.make(mToolbar, R.string.journal_setting_encrypt_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    mPsdInputView.setText("");
                    mPsdRepeatView.setText("");
                } else {
                    if (mPsdInputView.getText().toString().length() == 0) {
                        Snackbar.make(mToolbar, R.string.journal_setting_input_not_null, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        return;
                    }
                    mPsdTemp = Integer.parseInt(mPsdInputView.getText().toString());
                    if (mPsd == mPsdTemp) {
                        //当密码输入正确时
                        mPsdTemp = -1;  //赋值为-1，修改密码后可以进入if(mPsd==0||mPsdTemp==-1)代码块
                        mBtnDelete.setVisibility(View.VISIBLE);
                        mPsdInputView.setText("");
                        mPsdRepeatView.setText("");
                        mPsdInputView.setHint(R.string.journal_setting_input_6_digit_password);
                        mPsdRepeatView.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(mToolbar, R.string.journal_setting_not_match_old_password, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    }
                }
                break;
            case R.id.btn_psd_delete:
                JournalSqliteHelper.saveBudget(this, JOURNAL_PASSWORD, JOURNAL_PASSWORD, 0);
                mTitleText.setText(R.string.journal_setting_encrypt);
                mPsdSetArea.setVisibility(View.GONE);
                mBtnDelete.setVisibility(View.INVISIBLE);
                mPsd = 0;
                mPsdInputView.setText("");
                mPsdRepeatView.setText("");
                Snackbar.make(mToolbar, R.string.journal_setting_password_delete_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                break;
            case R.id.btn_cancel:
                mPsdInputView.setText("");
                mPsdRepeatView.setText("");
                mPsdTemp = -1;
                mPsdSetArea.setVisibility(View.GONE);
                break;
        }
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.journal_setting_confirm_clear_all_data);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mJournalManager.delExpenditureAndIncomeAll();
                Snackbar.make(mToolbar, R.string.journal_setting_data_clear_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mPsdSetArea.isShown()) {
            mPsdSetArea.setVisibility(View.GONE);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
