package com.ssdut.roysun.personalfinancialrecommendationsystem.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalSettingActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.AnimationDelay;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;

/**
 * Created by roysun on 16/3/12.
 * 指定功能加密提示框
 */
public class DialogInputPassword extends Dialog implements View.OnClickListener {

    private EditText mPasswordView;
    private Context mContext;
    private Activity mActivity;  // Activity对象  用户密码输入成功后关闭相应的Activity
    private View mDialogView;
    private Handler mHandler;
    private int mPassword = 0;
    @SuppressWarnings("rawtypes")
    private Class c;

    public DialogInputPassword(Context context, Activity activity, String whose) {
        super(context, R.style.main_dialog);
        mContext = context;
        mActivity = activity;
        mHandler = new Handler();
        mDialogView = View.inflate(context, R.layout.dialog_password, null);
        setContentView(mDialogView);
        mPasswordView = (EditText) mDialogView.findViewById(R.id.dialog_et_password);
        Button btnConfirm = (Button) mDialogView.findViewById(R.id.dialog_btn_confirm);
        btnConfirm.setOnClickListener(this);
        Button btncancel = (Button) mDialogView.findViewById(R.id.dialog_btn_cancel);
        btncancel.setOnClickListener(this);
        //当传入的whose为记账时
        if (whose.equals(JournalSettingActivity.JOURNAL_PASSWORD)) {
            mPasswordView.setHint("请输入记账工具密码");
            mPassword = JournalSqliteHelper.readPreferenceFile(context, JournalSettingActivity.JOURNAL_PASSWORD, JournalSettingActivity.JOURNAL_PASSWORD);
            c = JournalMainActivity.class;
        } else if (whose.equals(MemoSetPasswordDialog.BWMIMA)) {
            //当传入的whose为备忘时
            mPasswordView.setHint("请输入备忘记事密码");
            mPassword = JournalSqliteHelper.readPreferenceFile(context, MemoSetPasswordDialog.BWMIMA, MemoSetPasswordDialog.BWMIMA);
            c = MemoMainActivity.class;
        }
        show();
        //获取窗口 设置dialog位置为上部
        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        mDialogView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_confirm:
                String strPassword = mPasswordView.getText().toString();
                if (strPassword.equals("")) {
                    showMsg("输入不能为空");
                    return;
                }
                if (mPassword != 0) {
                    int password = Integer.parseInt(strPassword);
                    if (password == mPassword) {
                        cancel();
                        Intent intent = new Intent(mContext, c);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mContext.startActivity(intent);
                        AnimationDelay.dongHuaDialogEnd(this, mDialogView, mContext, mHandler, R.anim.push_up_out, 300);
                        if (mActivity != null) {
                            mActivity.finish();
                        }
                    } else {
                        showMsg("密码错误");
                    }
                }
                break;
            case R.id.dialog_btn_cancel:
                AnimationDelay.dongHuaDialogEnd(this, mDialogView, mContext, mHandler, R.anim.push_up_out, 300);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //dialog退出动画
                AnimationDelay.dongHuaDialogEnd(this, mDialogView, mContext, mHandler, R.anim.push_up_out, 300);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showMsg(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
