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
}
