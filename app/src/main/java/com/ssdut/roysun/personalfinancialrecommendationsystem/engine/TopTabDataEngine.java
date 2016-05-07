package com.ssdut.roysun.personalfinancialrecommendationsystem.engine;

import android.util.SparseArray;

import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.FinancingFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.TopTabInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roysun on 16/4/10.
 * 顶部tab数据引擎
 */
public class TopTabDataEngine {

    public static final String TAG = "TopTabDataEngine";

    public static final int TAB_FINANCING = 1;

    public static TopTabDataEngine mInstance;

    public SparseArray<TopTabContainer> tabContainerList = new SparseArray<TopTabContainer>();

    public static class TopTabContainer {

        public List<TopTabInfo> tabInfoList;  //存放各个tab信息
        public int type;

        /**
         * 构造默认tabcontainer。
         * @param type 主Fragment类型，可为FinancingFragment也可为其它
         */
        public TopTabContainer(int type) {
            this.type = type;
            switch (type) {
                case TAB_FINANCING:  //理财Fragment顶部tab
                    tabInfoList = new ArrayList<TopTabInfo>();
                    tabInfoList.add(new TopTabInfo("看盘", FinancingFragment.TYPE_OBSERVATION));
                    tabInfoList.add(new TopTabInfo("投资", FinancingFragment.TYPE_INVESTMENT));
                    tabInfoList.add(new TopTabInfo("推荐", FinancingFragment.TYPE_RECOMMENDATION));
                    break;
            }
        }

    }
}
