package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.UserManagementActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnItemTouchHelperMoveListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by roysun on 16/5/15.
 * 用户列表适配器
 */
public class UserListAdapter extends InfoListBaseAdapter implements OnItemTouchHelperMoveListener {

    public static final String TAG = "UserListAdapter";
    private final OnStartDragListener mStartDragListener;
    private ArrayList<User> mUserList = new ArrayList<>();  // 基类中没有需定义
    private boolean mIsDataChanged;

    public UserListAdapter(Context context, OnStartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        mContext = context;
        initInfoList();
    }

    @Override
    public void initInfoList() {
        // 初始化用户列表
        if (mContext instanceof UserManagementActivity
                && mUserList != null && mInfoIconList != null
                && mInfoTitleList != null && mInfoValueList != null) {
            mUserList.clear();
            mInfoIconList.clear();
            mInfoTitleList.clear();
            mInfoValueList.clear();
            mUserList = ((UserManagementActivity) mContext).getUserManager().getUserListFromDB("");
            for (User _user : mUserList) {
                if (_user.isSpecial() == 1) {
                    mInfoIconList.add(R.drawable.icon_special_account_hint);
                    mInfoTitleList.add("管理员");
                } else {
                    mInfoIconList.add(R.drawable.icon_username_hint);
                    mInfoTitleList.add("普通用户");
                }
                mInfoValueList.add(_user.getName());
            }
        }
    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final InfoItemViewHolder holder, final int position) {
        if (mIsDataChanged) {
            mIsDataChanged = false;
            initInfoList();
        }
        holder.mItemIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
        if (mUserList.get(position).isSpecial() == 0) {
            holder.mBtnItemUpdate.setVisibility(View.VISIBLE);
            holder.mBtnItemUpdate.setText("更 改 权 限");
            holder.mBtnItemUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof UserManagementActivity) {
                        final MaterialDialog _materialDialog = new MaterialDialog(mContext);
                        _materialDialog.setTitle("权限操作");
                        _materialDialog.setMessage("指定用户" + mUserList.get(position).getName() + "为管理员？");
                        _materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _materialDialog.dismiss();
                            }
                        });
                        _materialDialog.setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _materialDialog.dismiss();
                                ((UserManagementActivity) mContext).makeSpecial(mUserList.get(position));
                            }
                        });
                        _materialDialog.setCanceledOnTouchOutside(true).show();
                    }
                }
            });
        } else {
            holder.mBtnItemUpdate.setVisibility(View.GONE);  //一定要设置这里不然出莫名其妙的bug
        }
        holder.mItemIcon.setImageResource(mInfoIconList.get(position));
        holder.mItemTitleText.setText(mInfoTitleList.get(position));
        holder.mItemValueText.setText(mInfoValueList.get(position));
    }

    public void setDataChanged(boolean dataChanged) {
        mIsDataChanged = dataChanged;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mUserList, fromPosition, toPosition);
        Collections.swap(mInfoIconList, fromPosition, toPosition);
        Collections.swap(mInfoTitleList, fromPosition, toPosition);
        Collections.swap(mInfoValueList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        ((UserManagementActivity) mContext).deleteUser(mUserList.get(position));
        mUserList.remove(position);
        mInfoIconList.remove(position);
        mInfoTitleList.remove(position);
        mInfoValueList.remove(position);
        notifyItemRemoved(position);
    }
}
