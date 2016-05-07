package com.ssdut.roysun.personalfinancialrecommendationsystem.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ssdut.roysun.personalfinancialrecommendationsystem.service.binder.WeatherBinder;

/**
 * Created by roysun on 16/3/12.
 * 天气服务
 */

public class WeatherService extends Service {

    public final static String TAG = "WeatherService";

    public WeatherService() {
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate !!!");
        super.onCreate();
    }

    //startService()会执行这里，bindService()不会执行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand !!!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy !!!");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind !!!");
        return new WeatherBinder();
    }

}
