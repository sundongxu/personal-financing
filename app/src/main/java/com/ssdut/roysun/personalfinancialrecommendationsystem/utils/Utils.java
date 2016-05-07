package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by roysun on 16/4/26.
 * 自定义输入框工具类，包括SharedPreference、以及listview数目判断
 */
public class Utils {

    public static final String PRE_RECORDS = "HISTORY_RECORD";
    public static final String SEARCH_CONTENT = "SEARCH_CONTENT";

    public static final int REQUEST_CODE = 1234;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
