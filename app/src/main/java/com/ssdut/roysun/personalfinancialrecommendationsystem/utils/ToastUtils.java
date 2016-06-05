package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by roysun on 16/4/19.
 * Toast工具类
 */
public class ToastUtils {

    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMsg(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    // Snackbar不能完全替代Toast，前者依赖于视图，一旦页面finish掉了Snackbar就没了，Toast只与时间有关
    public static void showMsg(Context context, int resId) {
        String msg = context.getResources().getString(resId);
        showMsg(context, msg);
    }
}
