package com.ssdut.roysun.personalfinancialrecommendationsystem.network.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ssdut.roysun.personalfinancialrecommendationsystem.network.binder.TranslationBinder;

/**
 * Created by roysun on 16/3/12.
 * Service，网络操作，后台执行，调用有道翻译API
 */
public class TranslationService extends Service {

    public final static String TAG = "TranslationService";

    public TranslationService() {
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate !!!");
        super.onCreate();
    }

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
        return new TranslationBinder();
    }

}
