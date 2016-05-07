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
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MemoMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalMainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.JournalSettingActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;

/**
 * Created by roysun on 16/3/12.
 */
public class DialogShuRuMiMa extends Dialog implements View.OnClickListener {
    //密码输入框
    private EditText et = null;
    private Context context;
    //Activity对象  用户密码输入成功后关闭相应的Activity
    private Activity activity;
    //Dialog的View
    private View diaView;
    private Handler handler;
    private int mi = 0;
    @SuppressWarnings("rawtypes")
    private Class c;

    public DialogShuRuMiMa(Context context, Activity activity, String whose) {
        super(context, R.style.maindialog);
        this.context = context;
        this.activity = activity;
        handler = new Handler();
        diaView = View.inflate(context, R.layout.dialog_mima, null);
        this.setContentView(diaView);
        et = (EditText) diaView.findViewById(R.id.dialog_mima_et);
        Button quding = (Button) diaView.findViewById(R.id.dialog_mima_queding_bt);
        quding.setOnClickListener(this);
        Button quxiao = (Button) diaView.findViewById(R.id.dialog_mima_quxiao_bt);
        quxiao.setOnClickListener(this);
        //当传入的whose为记账时
        if (whose.equals(JournalSettingActivity.JOURNAL_PASSWORD)) {
            et.setHint("请输入记账工具密码");
            mi = JournalSqliteHelper.readPreferenceFile(context, JournalSettingActivity.JOURNAL_PASSWORD, JournalSettingActivity.JOURNAL_PASSWORD);
            c = JournalMainActivity.class;
        } else if (whose.equals(MemoSetPasswordDialog.BWMIMA)) {//当传入的whose为备忘时
            et.setHint("请输入备忘记事密码");
            mi = JournalSqliteHelper.readPreferenceFile(context, MemoSetPasswordDialog.BWMIMA, MemoSetPasswordDialog.BWMIMA);
            c = MemoMainActivity.class;
        }

        //添加备注编辑框
        this.show();
        //获取窗口 设置dialog位置为上部
        Window window = this.getWindow();
        window.setGravity(Gravity.TOP);
        diaView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_mima_queding_bt:
                String etString = et.getText().toString();
                if (etString.equals(null) || etString.equals("")) {
                    showMsg("输入不能为空");
                    return;
                }
                if (mi != 0) {
                    int mima = Integer.parseInt(etString);
                    if (mima == mi) {
                        this.cancel();
                        Intent intent = new Intent(context, c);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent);
                        DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                        if (activity != null) {
                            activity.finish();
                        }
                    } else {
                        showMsg("密码错误");
                    }
                }
                break;
            case R.id.dialog_mima_quxiao_bt:
                DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //dialog退出动画
                DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
