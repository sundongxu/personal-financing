package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.SharedPreferenceUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.Utils;

/**
 * Created by roysun on 16/4/29.
 * MD风格的登录界面
 * 注意切换Activiy动画
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    private Toolbar mToolbar;  // 顶部toolbar
    private ImageView mTopBGView;  // 顶部背景图
    private ImageView mUserNameIcon;
    private ImageView mPasswordIcon;
    private FloatLabel mUserNameView;  // 登录名
    private FloatLabel mPasswordView;  // 密码
    private FloatingActionButton mFabLogin;
    private CheckBox mRememberMeBox;  // 记住我勾选框
    private Button mBtnSignIn;  // 登录按钮
    private TextView mRegisterText;  // 点击注册
    private TextView mForgetPasswordText;  //点击验证密保问题，输入用户名、密码跳转到填写密保问题答案，验证成功后，跳转修改密码页面，修改成功后跳转登录界面

    private Context mContext;
    private boolean mIsRememberMe;  // 是否记住当前登录账户，保存登录态 -> sharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        mToolbar = (Toolbar) findViewById(R.id.tb_signin_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTopBGView = (ImageView) findViewById(R.id.iv_signin_header_bg);
        int h = TimeUtils.getHour();
        if (h > 18 || h < 6) {
            //根据时间决定显示白天还是夜晚的顶部背景图
            mTopBGView.setImageResource(R.drawable.bg_signin_night);
        }
        mUserNameIcon = (ImageView) findViewById(R.id.iv_username_hint);
        mPasswordIcon = (ImageView) findViewById(R.id.iv_password_hint);
        mUserNameView = (FloatLabel) findViewById(R.id.et_user_name);
        mPasswordView = (FloatLabel) findViewById(R.id.et_password);
        mUserNameView.getEditText().setTextColor(R.color.black);
        mPasswordView.getEditText().setTextColor(R.color.black);
        mUserNameView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint_input);
//                    mUserNameIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_username_hint_input));
                } else {
                    mUserNameIcon.setImageResource(R.drawable.icon_username_hint);
//                    mUserNameIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_username_hint));
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

        mRememberMeBox = (CheckBox) findViewById(R.id.cb_remember_me);
        mRememberMeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 多选按钮是否勾选应该在点击登录按钮的动作中做记住上次登录名的操作
                if (isChecked) {
                    mIsRememberMe = true;
                } else {
                    mIsRememberMe = false;
                }
            }
        });

        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mRegisterText = (TextView) findViewById(R.id.tv_register_now);
        mForgetPasswordText = (TextView) findViewById(R.id.tv_forget_password);
        mBtnSignIn.setOnClickListener(this);
        mRegisterText.setOnClickListener(this);
        mForgetPasswordText.setOnClickListener(this);
        mFabLogin = (FloatingActionButton) findViewById(R.id.fab_signin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                if (mUserManager.isSignIn() && mUserManager.getCurUser() != null) {
                    // 已经登录过，请先注销
                    ToastUtils.showMsg(mContext, "你已登录过，请先注销！");
                } else {
                    String _userName = mUserNameView.getEditText().getText().toString();
                    String _password = mPasswordView.getEditText().getText().toString();
                    if (_userName.equals("") || _password.equals("")) {
                        if (_userName.equals("") && _password.equals("")) {
                            ToastUtils.showMsg(mContext, "用户名和密码输入不能为空！");
                        } else {
                            String _strNull = _userName.equals("") ? "用户名" : "密码";
                            ToastUtils.showMsg(mContext, _strNull + "输入不能为空！");
                        }
                    } else {
                        // 用户名和密码均非空，登录操作
                        mUserManager.signIn(_userName, _password);
                        if (mUserManager.isSignIn()) {
                            if (mUserManager.isSpecialAccount() == 1) {
                                ToastUtils.showMsg(mContext, "管理员登录成功！");
                                rememberUser();
                                finish();
                            } else {
                                ToastUtils.showMsg(mContext, "登录成功！");
                                rememberUser();
                                finish();
                            }
                        } else {
                            //登录失败
                            if (mUserManager.isUserExists(_userName)) {
                                ToastUtils.showMsg(mContext, "密码错误！");
                            } else {
                                ToastUtils.showMsg(mContext, "用户名不存在！");
                            }
                        }
                    }
                }
                break;
            case R.id.tv_register_now:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    public void rememberUser() {
        if (mIsRememberMe) {
            SharedPreferenceUtils.addToList(mContext,
                    Utils.LOGIN_HISTORY, Utils.USERNAME_LAST_LOGIN, mUserNameView.getEditText().getText().toString());
            SharedPreferenceUtils.addToList(mContext,
                    Utils.LOGIN_HISTORY, Utils.PASSWORD_LAST_LOGIN, mPasswordView.getEditText().getText().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
