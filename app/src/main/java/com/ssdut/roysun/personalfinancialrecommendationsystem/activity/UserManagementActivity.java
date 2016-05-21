package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info.UserListAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.callback.ItemTouchHelperCallback;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.DividerItemDecoration;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnStartDragListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ToastUtils;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by roysun on 16/4/5
 * 只对管理员可见的用户管理页面
 * 包括删除用户、指定特定用户为管理员
 * 移位和横滑删除效果4个helper类随时复原
 */
public class UserManagementActivity extends BaseActivity implements OnStartDragListener {

    public static final String TAG = "UserManagementActivity";

    private RecyclerView mUserList;
    private UserListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        initData();
        initView();
        showHintDialog();
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
            mToolbar.setTitle("用户管理");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mUserList = (RecyclerView) findViewById(R.id.rv_user_list);
        mUserList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mUserList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mUserList.setItemAnimator(new DefaultItemAnimator());
        mUserList.setLayoutManager(mLayoutManager);
        mAdapter = new UserListAdapter(mContext, this);
        mUserList.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mUserList);
    }

    public void showHintDialog() {
        final MaterialDialog _materialDialog = new MaterialDialog(this);
//        _materialDialog.setTitle("谨慎操作");
        _materialDialog.setMessage("左右横滑可删除用户!");
        _materialDialog.setPositiveButton("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _materialDialog.dismiss();
            }
        });
        _materialDialog.setCanceledOnTouchOutside(true).show();

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

    public void deleteUser(User user) {
        if (user != null) {
            String _userName = user.getName();
            if (mUserManager.deleteUser(user.getId()) > 0) {
                ToastUtils.showMsg(this, "用户" + _userName + "删除成功");
            } else {
                ToastUtils.showMsg(this, "用户" + _userName + "删除失败");
            }
        }
    }

    public void makeSpecial(User user) {
        if (user != null && user.isSpecial() == 0) {
            user.setSpecial(1);
            if (mUserManager.updateUserInfo(user, user.getId()) == 1) {
                //  之前就保证了去重逻辑，数据库中用户名唯一
                ToastUtils.showMsg(this, "权限修改成功！");
                mAdapter.setDataChanged(true);
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showMsg(this, "权限修改失败！");
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
