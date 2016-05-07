package com.ssdut.roysun.personalfinancialrecommendationsystem.component;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by roysun on 16/5/3.
 * 抽屉打开时拦截Touch事件，让抽屉菜单自行消化事件不往子视图下传，解决点击菜单透传Touch事件到下面的CardView响应
 */
public class InterceptTouchDrawerLayout extends DrawerLayout {

    private Context mContext;
    private boolean isShouldIntercept;

    public InterceptTouchDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isShouldIntercept;
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        isShouldIntercept = shouldIntercept;
    }
}
