package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnItemTouchHelperMovedListener;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/13.
 * 信息List的适配器基类
 */
public class InfoListBaseAdapter extends RecyclerView.Adapter<InfoListBaseAdapter.InfoItemViewHolder> {

    public static final String TAG = "InfoListBaseAdapter";

    // 用户信息
    public static final int INFO_USER_NAME = 7111;
    public static final int INFO_USER_PASSWORD = 7121;
    public static final int INFO_USER_PASSWORD_RESET = 7122;
    public static final int INFO_USER_BALANCE = 7131;
    public static final int INFO_USER_BALANCE_RECHARGE = 7132;
    public static final int INFO_USER_SECURITY_QUESTION = 7141;
    public static final int INFO_USER_SECURITY_QUESTION_RESET = 7142;
    public static final int INFO_USER_SECURITY_ANSWER_RESET = 7143;
    public static final int INFO_USER_CREATE_TIME = 7151;
    public static final int INFO_USER_UPDATE_TIME = 7161;
    public static final int INFO_USER_IS_SPECIAL = 7171;

    public static final int INFO_USER_NORMAL = 7211;
    public static final int INFO_USER_SPECIAL = 7221;

    // 设备信息
    public static final int INFO_DEVICE_ = 7311;

    // 应用信息
    public static final int INFO_APP_ = 7411;

    public ArrayList<Integer> mInfoIconList = new ArrayList<>();
    public ArrayList<String> mInfoTitleList = new ArrayList<>();
    public ArrayList<String> mInfoValueList = new ArrayList<>();
    public Context mContext;
    public int mItemType;

    public void initInfoList() {

    }

    @Override
    public int getItemCount() {
        return mInfoTitleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(InfoItemViewHolder holder, int position) {

    }

    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false);
        InfoItemViewHolder viewHolder = new InfoItemViewHolder(view);
        return viewHolder;
    }

    public class InfoItemViewHolder extends RecyclerView.ViewHolder implements OnItemTouchHelperMovedListener {
        // 用户信息条目内容
        public RelativeLayout mItemArea;
        public ImageView mItemIcon;
        public TextView mItemTitleText;
        public TextView mItemValueText;
        public FloatLabel mItemUpdateView;
        public Button mBtnItemUpdate;

        public InfoItemViewHolder(View v) {
            super(v);
            mItemArea = (RelativeLayout) v.findViewById(R.id.rl_info_item);
            mItemIcon = (ImageView) v.findViewById(R.id.iv_info_icon);
            mItemTitleText = (TextView) v.findViewById(R.id.tv_info_title);
            mItemValueText = (TextView) v.findViewById(R.id.tv_info_value);
            mItemUpdateView = (FloatLabel) v.findViewById(R.id.et_info_update_content);
            mBtnItemUpdate = (Button) v.findViewById(R.id.btn_info_update);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
