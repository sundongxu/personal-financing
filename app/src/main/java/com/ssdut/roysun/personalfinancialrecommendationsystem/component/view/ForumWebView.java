package com.ssdut.roysun.personalfinancialrecommendationsystem.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by roysun on 16/4/24.
 * WebView组件，加入INTERNET权限
 */
public class ForumWebView extends WebView {
    public ForumWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ForumWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForumWebView(Context context) {
        super(context);
    }
}
