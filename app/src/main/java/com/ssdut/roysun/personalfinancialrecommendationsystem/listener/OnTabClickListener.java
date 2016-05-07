package com.ssdut.roysun.personalfinancialrecommendationsystem.listener;

import android.view.View;

/**
 * Created by roysun on 16/4/9.
 */
public interface OnTabClickListener {
    public final static int TYPE_VIRTUAL = 0;
    public final static int TYPE_REAL = 1;
    public void onTabClick(View view, int type);
}
