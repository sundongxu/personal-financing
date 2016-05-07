package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by roysun on 16/4/28.
 * 网络工具类
 */
public class NetworkUtils {

    //网络状态常量
    public static final int ACCESS_SUCCESS = 1010;//获取成功
    public static final int ACCESS_FAIL = 1020;//获取失败
    public static final int NO_INTERNET = 1030;//没有网络

    /**
     * 判断网络连接状态
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager _cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = _cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
