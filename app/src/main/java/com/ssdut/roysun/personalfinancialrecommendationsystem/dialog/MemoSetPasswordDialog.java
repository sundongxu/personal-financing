package com.ssdut.roysun.personalfinancialrecommendationsystem.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.AnimationDelay;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;

/**
 * Created by roysun on 16/3/12.
 *
 */
public class MemoSetPasswordDialog extends Dialog implements View.OnClickListener {
    public static final String BWMIMA = "BWMIMA";
    //密码  和密码标识
    int mi = 0, tempmi = 0;
    //密码输入框
    private EditText mima1, mima2;
    //设置密码标题
    private TextView title;
    //确定  取消  删除按钮
    private Button queding, quxiao, shanchu;
    //上下文对象
    private Context context;
    //Dialog的View
    private View diaView;
    private Handler handler;

    public MemoSetPasswordDialog(Context context) {
        super(context, R.style.main_dialog);
        this.context = context;
        handler = new Handler();
        diaView = View.inflate(context, R.layout.dialog_bwmima, null);
        this.setContentView(diaView);
        title = (TextView) diaView.findViewById(R.id.dialog_bwtittle_text);
        mima1 = (EditText) diaView.findViewById(R.id.dialog_bwmima_et1);
        mima2 = (EditText) diaView.findViewById(R.id.dialog_bwmima_et2);
        queding = (Button) diaView.findViewById(R.id.dialog_bwmima_queding_bt);
        queding.setOnClickListener(this);
        shanchu = (Button) diaView.findViewById(R.id.dialog_bwmima_shanchu_bt);
        shanchu.setOnClickListener(this);
        quxiao = (Button) diaView.findViewById(R.id.dialog_bwmima_quxiao_bt);
        quxiao.setOnClickListener(this);
        mi = JournalSqliteHelper.readPreferenceFile(context, BWMIMA, BWMIMA);
        if (mi == 0) {//密码为0则当前没密码
            title.setText("设置程序密码");
            shanchu.setVisibility(View.INVISIBLE);
        } else {
            title.setText("修改或删除密码");
            tempmi = mi;
            mima1.setHint("请输入旧密码");
            mima2.setVisibility(View.GONE);//密码确认框不可见
            shanchu.setVisibility(View.INVISIBLE);//删除密码按钮不可见
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
            case R.id.dialog_bwmima_queding_bt://确定
                if (mi == 0 || tempmi == -1) {//当没有密码时
                    if (mima1.getText().toString().length() == 0 || mima2.getText().toString().length() == 0) {
                        showMsg("输入不能为空");
                        return;
                    }
                    if (mima1.getText().toString().length() != 6 || mima2.getText().toString().length() != 6) {
                        showMsg("密码必须为六位数");
                        return;
                    }
                    if (!mima1.getText().toString().equals(mima2.getText().toString())) {
                        showMsg("两次输入的密码不匹配");
                        return;
                    }
                    mi = Integer.parseInt(mima2.getText().toString());
                    tempmi = mi;
                    JournalSqliteHelper.saveBudget(context, BWMIMA, BWMIMA, mi);
                    title.setText("修改或删除密码");
                    AnimationDelay.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                    showMsg("加密成功");
                    mima1.setText("");
                    mima2.setText("");
                } else {
                    if (mima1.getText().toString().length() == 0) {
                        showMsg("输入不能为空");
                        return;
                    }
                    tempmi = Integer.parseInt(mima1.getText().toString());
                    if (mi == tempmi) {//当密码输入正确时
                        tempmi = -1;//赋值为-1修改密码后可以进入if(mi==0||tempmi==-1)代码块
                        shanchu.setVisibility(View.VISIBLE);
                        mima1.setText("");
                        mima2.setText("");
                        mima1.setHint("请输入新密码（六位）");
                        mima2.setVisibility(View.VISIBLE);
                    } else {
                        showMsg("与旧密码不匹配");
                    }
                }
                break;
            case R.id.dialog_bwmima_shanchu_bt:
                JournalSqliteHelper.saveBudget(context, BWMIMA, BWMIMA, 0);
                title.setText("程序加密");
                shanchu.setVisibility(View.INVISIBLE);
                mi = 0;
                mima1.setText("");
                mima2.setText("");
                showMsg("删除成功");
                AnimationDelay.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                break;
            case R.id.dialog_bwmima_quxiao_bt:
                mima1.setText("");
                mima2.setText("");
                tempmi = -1;
                AnimationDelay.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //dialog退出动画
                AnimationDelay.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
