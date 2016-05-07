package com.ssdut.roysun.personalfinancialrecommendationsystem.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHuaYanChi;

/**
 * Created by roysun on 16/3/12.
 */
public class DialogAbout extends Dialog {
    private View diaView;
    private Context context;
    private Handler handler;

    // flag标识转发还是评论，id标识当前微博的id，where标识从哪里启动该dialog，两个地方可以启动，home界面，和内容界面
    public DialogAbout(Context context, String text) {
        super(context, R.style.maindialog);
        this.context = context;
        handler = new Handler();
        diaView = View.inflate(context, R.layout.dialog_about, null);
        this.setContentView(diaView);
        TextView textView = (TextView) diaView.findViewById(R.id.dialog_about_text);
        textView.setText(text);
//		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        //添加备注编辑框
        this.show();
        diaView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.push_up_in));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                DongHuaYanChi.dongHuaDialogEnd(this, diaView, context, handler, R.anim.push_up_out, 300);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
