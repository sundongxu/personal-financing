package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

/**
 * Created by roysun on 16/4/29.
 * MD风格的忘记密码界面
 */
public class ForgetPasswordActivity extends BaseActivity {

    public static final String TAG = "ForgetPasswordActivity";

    private Button mButton;
    private LinearLayout mSecurityQuestionArea;
    private LinearLayout mSecurityAnswerArea;
    private LinearLayout mPasswordArea;
    private LinearLayout mPasswordRepeatArea;
    private ImageView mUserNameIcon;
    private ImageView mPasswordIcon;
    private ImageView mPasswordRepeatIcon;
    private ImageView mSecurityAnswerIcon;
    private FloatLabel mUserNameView;
    private FloatLabel mSecurityQuestionView;
    private FloatLabel mSecurityAnswerView;
    private FloatLabel mPasswordView;
    private FloatLabel mPasswordRepeatView;

    private User mUser;  //  忘记密码的用户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_forget_password_page);  // 继承自父类的通用toolbar
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSecurityQuestionArea = (LinearLayout) findViewById(R.id.ll_security_question);
        mSecurityAnswerArea = (LinearLayout) findViewById(R.id.ll_security_answer);
        mPasswordArea = (LinearLayout) findViewById(R.id.ll_password_reset);
        mPasswordRepeatArea = (LinearLayout) findViewById(R.id.ll_password_reset_confirm);

        mUserNameIcon = (ImageView) findViewById(R.id.iv_user_name_hint);
        mSecurityAnswerIcon = (ImageView) findViewById(R.id.iv_security_answer_hint);
        mPasswordIcon = (ImageView) findViewById(R.id.iv_password_reset_hint);
        mPasswordRepeatIcon = (ImageView) findViewById(R.id.iv_password_reset_confirm_hint);

        mUserNameView = (FloatLabel) findViewById(R.id.et_user_name);
        mSecurityQuestionView = (FloatLabel) findViewById(R.id.et_security_question);
        mSecurityAnswerView = (FloatLabel) findViewById(R.id.et_security_answer);
        mPasswordView = (FloatLabel) findViewById(R.id.et_password_reset);
        mPasswordRepeatView = (FloatLabel) findViewById(R.id.et_password_reset_confirm);

        mUserNameView.getEditText().setTextColor(R.color.black);
        mSecurityQuestionView.getEditText().setTextColor(R.color.black);
        mSecurityAnswerView.getEditText().setTextColor(R.color.black);
        mPasswordView.getEditText().setTextColor(R.color.black);
        mPasswordRepeatView.getEditText().setTextColor(R.color.black);

        mUserNameView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint_input);
                } else {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint);
                }
            }
        });

        mSecurityAnswerView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSecurityAnswerIcon.setImageResource(R.drawable.icon_security_answer_hint_input);
                } else {
                    mSecurityAnswerIcon.setImageResource(R.drawable.icon_security_answer_hint);
                }
            }
        });

        mPasswordView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPasswordIcon.setImageResource(R.drawable.icon_password_hint_input);
                } else {
                    mPasswordIcon.setImageResource(R.drawable.icon_password_hint);
                }
            }
        });

        mPasswordRepeatView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPasswordRepeatIcon.setImageResource(R.drawable.icon_password_hint_input);
                } else {
                    mPasswordRepeatIcon.setImageResource(R.drawable.icon_password_hint);
                }
            }
        });

        mButton = (Button) findViewById(R.id.btn_forget_password);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  //点击按钮就隐藏软键盘
                if (mSecurityQuestionArea.getVisibility() == View.GONE) {
                    String _userName = mUserNameView.getEditText().getText().toString();
                    String _securityQuestion;
                    if (!_userName.equals("")) {
                        _securityQuestion = getSecurityQuestion(_userName);
                        if (!_securityQuestion.equals("")) {
                            mSecurityQuestionView.setText(_securityQuestion);
                            mSecurityQuestionView.getEditText().setEnabled(false);  // 让EditText永远无法获得焦点
                            mSecurityQuestionView.getEditText().setFocusable(false);
                            mUserNameView.getEditText().setFocusable(false);
                            mUserNameView.getEditText().setEnabled(false);
                            mUserNameView.setLabel("");
                            mSecurityQuestionArea.setVisibility(View.VISIBLE);
                            mSecurityAnswerArea.setVisibility(View.VISIBLE);
                            mButton.setText(R.string.forget_password_verify);
                        } else {
                            Snackbar.make(mToolbar, R.string.login_username_not_exists, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        }
                    } else {
                        Snackbar.make(mToolbar, R.string.forget_password_username_not_null, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    }
                } else if (mPasswordArea.getVisibility() == View.GONE) {
                    String _securityAnswer = mSecurityAnswerView.getEditText().getText().toString();
                    if (!_securityAnswer.equals("") && isAnswerCorrect(_securityAnswer)) {
                        mSecurityAnswerView.getEditText().setFocusable(false);
                        mSecurityAnswerView.getEditText().setEnabled(false);
                        mSecurityAnswerView.setLabel("");
                        mPasswordArea.setVisibility(View.VISIBLE);
                        mPasswordRepeatArea.setVisibility(View.VISIBLE);
                        mButton.setText(R.string.forget_password_reset_password);
                    } else if (_securityAnswer.equals("")) {
                        Snackbar.make(mToolbar, R.string.forget_password_security_answer_not_null, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    } else {
                        Snackbar.make(mToolbar, R.string.forget_password_security_answer_wrong, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    }
                } else {
                    // 修改密码
                    String _newPassword = mPasswordView.getEditText().getText().toString();
                    String _newPasswordRepeat = mPasswordRepeatView.getEditText().getText().toString();
                    if (!_newPassword.equals("") && !_newPasswordRepeat.equals("")) {
                        if (!_newPasswordRepeat.equals(_newPassword)) {
                            Snackbar.make(mToolbar, R.string.journal_setting_password_repeat_not_match, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        } else {
                            resetPassword(_newPassword);
                        }
                    } else {
                        Snackbar.make(mToolbar, R.string.forget_password_input_password, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                    }
                }

            }
        });
    }

    private String getSecurityQuestion(String userName) {
        mUser = mUserManager.getUserFromDB(userName);
        return mUser != null ? mUser.getQuestion() : "";
    }

    private boolean isAnswerCorrect(String securityAnswer) {
        return mUser.getAnswer().equals(securityAnswer);
    }

    private void resetPassword(String password) {
        //  不能和旧密码一样
        if (!mUser.getPassword().equals(password)) {
            mUser.setPassword(password);
            mUser.setUpdateTime(TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay());
            if (mUserManager.updateUserInfo(mUser, mUser.getId()) == 1) {
                //  之前就保证了去重逻辑，数据库中用户名唯一
                Snackbar.make(mToolbar, R.string.forget_password_reset_password_success, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
            } else {
                Snackbar.make(mToolbar, R.string.forget_password_reset_password_failure, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
            }
        } else {
            Snackbar.make(mToolbar, R.string.forget_password_same, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
