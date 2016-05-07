package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.BaseActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.UserManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/4/29.
 * MD风格的登录界面，返回主Activity的切换动画有点奇怪
 */
public class LoginActivityMD extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivityMD";

    private Toolbar mToolbar;  // 顶部toolbar
    private ImageView mTopBGView;  // 顶部背景图
    private EditText mUserNameView;  // 登录名
    private EditText mPasswordView;  // 密码
    private FloatingActionButton mFabLogin;
    private CheckBox mRememberMeBox;  // 记住我勾选框
    private Button mBtnSignIn;  // 登录按钮
    private TextView mRegisterText;  // 点击注册
    private TextView mForgetPasswordText;  //点击验证密保问题，输入用户名、密码跳转到填写密保问题答案，验证成功后，跳转修改密码页面，修改成功后跳转登录界面
    private NestedScrollView mNestedScrollView;

    private Context mContext;
    private UserManager mUserManager;  //用户管理
    private int mRememberMe;  // 是否记住当前登录账户，保存登录态 -> sharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_md);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mUserManager = UserManager.getInstance(getApplicationContext());
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
//            mTopBGView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_signin_night));
        }
        mUserNameView = (EditText) findViewById(R.id.et_user_name);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mRegisterText = (TextView) findViewById(R.id.tv_register_now);
        mForgetPasswordText = (TextView) findViewById(R.id.tv_forget_password);
        mBtnSignIn.setOnClickListener(this);
        mRegisterText.setOnClickListener(this);
        mForgetPasswordText.setOnClickListener(this);
        mFabLogin = (FloatingActionButton) findViewById(R.id.fab_signin);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nsv_signin_main_area);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                if (mUserManager.getCurUser() != null){
                    // 已经登录过，请先注销
                    ToastUtils.showMsg(mContext, "你已登录过，请先注销！");
                } else {
                    String _userName = mUserNameView.getText().toString();
                    String _password = mPasswordView.getText().toString();
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
                            if (mUserManager.isSpecialAccount() == 1){
                                ToastUtils.showMsg(mContext, "管理员登录成功！");
                            } else {
                                ToastUtils.showMsg(mContext, "登录成功！");
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
                startActivity(new Intent(LoginActivityMD.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivityMD.this, ForgetPasswordActivity.class));
                break;
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
//                startActivity(new Intent(LoginActivityMD.this, MainActivityMD.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
