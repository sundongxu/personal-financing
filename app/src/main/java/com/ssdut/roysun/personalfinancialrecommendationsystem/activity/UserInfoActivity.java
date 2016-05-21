package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.UserInfoListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

/**
 * Created by roysun on 16/4/5
 * RecycleView主视图的用户个人信息页面，形式可在“账户管理”、“关于”、“设备信息”等页面复用
 * 包括修改三个用户属性：密码、密保问题及答案、充值
 */
public class UserInfoActivity extends BaseActivity {

    public static final String TAG = "UserInfoActivity";

    private RecyclerView mUserInfoList;
    private UserInfoListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.info_user);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserInfoList = (RecyclerView) findViewById(R.id.rv_user_info_list);
        mUserInfoList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUserInfoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mUserInfoList.setItemAnimator(new DefaultItemAnimator());
        mUserInfoList.setLayoutManager(mLayoutManager);
        mAdapter = new UserInfoListAdapter(this);
        mUserInfoList.setAdapter(mAdapter);
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

    public void resetPassword(String password) {
        User _curUser = mUserManager.getCurUser();
        if (!_curUser.getPassword().equals(password)) {
            _curUser.setPassword(password);
            _curUser.setUpdateTime(TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay());
            if (mUserManager.updateUserInfo(_curUser, _curUser.getId()) == 1) {
                //  之前就保证了去重逻辑，数据库中用户名唯一
                ToastUtils.showMsg(this, "密码修改成功！");
                finish();
            } else {
                ToastUtils.showMsg(this, "密码修改失败！");
            }
        } else {
            ToastUtils.showMsg(this, "新密码不能为之前一次的旧密码！");
        }
    }

    public void resetSecurityInfo(String question, String answer) {
        User _curUser = mUserManager.getCurUser();
        if (!_curUser.getQuestion().equals(question)) {
            _curUser.setQuestion(question);
            _curUser.setAnswer(answer);
            _curUser.setUpdateTime(TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay());
            if (mUserManager.updateUserInfo(_curUser, _curUser.getId()) == 1) {
                //  之前就保证了去重逻辑，数据库中用户名唯一
                ToastUtils.showMsg(this, "密保问题修改成功！");
                finish();
            } else {
                ToastUtils.showMsg(this, "密保问题修改失败！");
            }
        } else {
            ToastUtils.showMsg(this, "新密保问题不能为前一次的旧问题！");
        }
    }

    public void recharge(String sBalanceAdd) {
        User _curUser = mUserManager.getCurUser();
        double dBalanceAdd = Double.valueOf(sBalanceAdd);
        _curUser.setBalance(_curUser.getBalance() + dBalanceAdd);
        _curUser.setUpdateTime(TimeUtils.getYear() + "." + TimeUtils.getMonth() + "." + TimeUtils.getDay());
        if (mUserManager.updateUserInfo(_curUser, _curUser.getId()) == 1) {
            //  之前就保证了去重逻辑，数据库中用户名唯一
            ToastUtils.showMsg(this, "充值成功！");
            finish();
        } else {
            ToastUtils.showMsg(this, "充值失败！");
        }
    }
}
