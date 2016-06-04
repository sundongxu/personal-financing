package com.ssdut.roysun.personalfinancialrecommendationsystem.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalAddActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.journal.JournalCategoryAdapter;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.Animation3D;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.AnimationDelay;

/**
 * Created by roysun on 16/3/12.
 * 类别对话框
 */
public class DialogCategory extends Dialog implements View.OnClickListener {
    //类别和类别子类标识
    public static final int FLAG_CATEGORY = 3020;
    String mFlagShow;  // 判断当前该列表是否已显示
    Handler mHandler = new Handler();
    //类别列表和类别子类列表
    private ListView mCategoryList, mSubCategoryList;
    //顶部背景
    private RelativeLayout mTopLayout;
    private Context mContext;
    private View mCategoryView;
    //类别列表适配器
    private JournalCategoryAdapter mJournalCategoryAdapter;
    //当前选择的选项，收入，支出，借贷
    private int mNowFlag = 0;

    public DialogCategory(Context context, int nowFlag) {
        super(context, R.style.leibiedialog);
        mContext = context;
        mNowFlag = nowFlag;
        mCategoryView = View.inflate(context, R.layout.dialog_leibie, null);
        setContentView(mCategoryView);
        //类别适配器
        mJournalCategoryAdapter = new JournalCategoryAdapter(context, nowFlag, null);
        //类别列表
        mCategoryList = (ListView) mCategoryView.findViewById(R.id.leibie_dialog_list);
        mCategoryList.setAdapter(mJournalCategoryAdapter);
        mCategoryList.setOnItemClickListener(new ItemClickListener());
        //类别子类列表
        mSubCategoryList = (ListView) mCategoryView.findViewById(R.id.leibie_dialog_sub_list);
        mSubCategoryList.setVisibility(View.GONE);
        mTopLayout = (RelativeLayout) mCategoryView.findViewById(R.id.leibie_dialog_rl);
        mTopLayout.setOnClickListener(this);
        show();
        mCategoryView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
    }

    @Override
    public void onClick(View v) {
    }

    public void getTextandSend(View view) {
        String string = (String) view.getTag();
        //dialog退出动画
        AnimationDelay.dongHuaDialogEnd(this, mCategoryView, mContext, mHandler, R.anim.push_up_out, 300);
        Message msg = Message.obtain();
        msg.what = JournalAddActivity.CATEGORY_MSG;
        msg.obj = string;//发送当前选择给ToolsJiZhangAddActivity，让其界面作出相应改变
        if (mContext instanceof JournalAddActivity) {
            ((JournalAddActivity) mContext).mMsgHandler.sendMessage(msg);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mCategoryView.isShown()) {
                    AnimationDelay.dongHuaDialogEnd(this, mCategoryView, mContext, mHandler, R.anim.push_up_out, 300);
                    return false;
                } else {
                    cancel();
                }
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mNowFlag == JournalAddActivity.EXPENDITURE) {
                String _categoryStr = (String) view.getTag();
                mSubCategoryList.setAdapter(new JournalCategoryAdapter(mContext, FLAG_CATEGORY, _categoryStr));
                if (mSubCategoryList.isShown() && mFlagShow.equals(_categoryStr)) {
                    //主list恢复动画
                    mCategoryList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.picpush_right_in));
                    //子list隐藏
                    mSubCategoryList.setVisibility(View.GONE);
                } else {
                    //主list收缩动画
                    mCategoryList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.picpush_left_out));
                    //子list进入动画
                    mSubCategoryList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
                    mSubCategoryList.setVisibility(View.VISIBLE);
                    mSubCategoryList.setLayoutAnimation(Animation3D.listDongHua());
                }
                mFlagShow = _categoryStr;
                mSubCategoryList.setOnItemClickListener(new SubItemClickListener());
            } else if (mNowFlag == JournalAddActivity.INCOME) {
                getTextandSend(view);
            } else if (mNowFlag == JournalAddActivity.CREDIT_DEBIT) {
                getTextandSend(view);
            }
        }
    }

    // 子条目点击事件
    private class SubItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getTextandSend(view);
        }
    }
}
