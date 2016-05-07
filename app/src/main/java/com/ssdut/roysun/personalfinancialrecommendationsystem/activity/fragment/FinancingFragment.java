package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.TopTabInfo;
import com.ssdut.roysun.personalfinancialrecommendationsystem.callback.MainFragmentCallback;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.TopTabBarView;
import com.ssdut.roysun.personalfinancialrecommendationsystem.engine.TopTabDataEngine;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnTabClickListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by roysun on 16/4/1.
 * No.2 Fragment，理财
 */
public class FinancingFragment extends BaseFragment {

    public static final String TAG = "FinancingFragment";

    //顶部3个tab类型
    public static final int TYPE_OBSERVATION = 201;
    public static final int TYPE_INVESTMENT = 202;
    public static final int TYPE_RECOMMENDATION = 203;

    private TopTabBarView mTopTab;
    private ViewPager mViewPager;
    private TopTabPagerAdapter mTabPagerAdapter;
    private View mTabLayout;
    private View mRootContentView;

    private int currIndex = 0;

    boolean isClickTab = false;

    private MainFragmentCallback mListener;  //

    private TopTabDataEngine.TopTabContainer mTopTabContainer;

    public FinancingFragment() {
        super(MainActivity.getInstance());
    }

    public static FinancingFragment newInstance(String param1, String param2) {
        FinancingFragment fragment = new FinancingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopTabContainer = new TopTabDataEngine.TopTabContainer(TopTabDataEngine.TAB_FINANCING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootContentView = inflater.inflate(R.layout.fragment_financing, container, false);
        return mRootContentView;
    }

    boolean bFirstOnresume = true;

    @Override
    public void onPageResume(boolean isSnapResume) {
//        super.onPageResume(isSnapResume);
        if (mRootContentView == null) {
            return;
        }
//        initDefaultTabSelected();  //根据传入的选中类型参数修改初始tab的index，即决定一开始展示哪个tab
        if (bFirstOnresume) {
            bFirstOnresume = false;
            mTopTab = (TopTabBarView) mRootContentView.findViewById(R.id.top_tab);
            mViewPager = (ViewPager) mRootContentView.findViewById(R.id.viewpager_financing_tab);
            mViewPager.setOffscreenPageLimit(4);
            mTabLayout = mRootContentView.findViewById(R.id.navigationbar);
            initTabs();
            // 初始化viewpager
            if (mTabPagerAdapter == null) {
                mTabPagerAdapter = new TopTabPagerAdapter(getChildFragmentManager(), mTopTabContainer.tabInfoList);
            }
            mViewPager.setAdapter(mTabPagerAdapter);
            mViewPager.setCurrentItem(currIndex);
            mViewPager.setOnPageChangeListener(new OnTopTabPageChangeListener());

        } else {
            mTopTab.init(currIndex);
            mViewPager.setCurrentItem(currIndex, false);
        }
        if (mViewPager != null && mTabPagerAdapter.getItem(currIndex) != null) {
            BaseFragment fragment = (BaseFragment) mTabPagerAdapter.getItem(currIndex);
            fragment.onPageResume(isSnapResume);
        }
    }

    private void initTabs() {
        String[] titles = new String[]{"看盘", "投资", "推荐"};
//            for (int i = 0; i < tabContainer.tabInfoList.size(); i++) {
//                CftGetNavigationEngine.AppTabInfo tabInfo = tabContainer.tabInfoList.get(i);
//                titles[i] = tabInfo.name;
//            }

        mTopTab.setTabs(titles);
        mTopTab.init(currIndex);
        mTopTab.setTabClickListener(mOnTabClickListener);
    }

    private OnTabClickListener mOnTabClickListener = new OnTabClickListener() {

        @Override
        public void onTabClick(View view, int type) {
            //添加上报
            isClickTab = true;
            mViewPager.setCurrentItem(view.getId() - TopTabBarView.START_ID_INDEX);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentCallback) {
            mListener = (MainFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class TopTabPagerAdapter extends FragmentPagerAdapter {
        // 外面传递过来的tab列表数据
        private List<TopTabInfo> curTabDataList;
        private SparseArray<WeakReference<Fragment>> viewCache = new SparseArray<WeakReference<Fragment>>();

        public TopTabPagerAdapter(FragmentManager fm, final List<TopTabInfo> currentTabDataList) {
            super(fm);
            this.curTabDataList = currentTabDataList;
        }

        @Override
        public Fragment getItem(int postion) {
            TopTabInfo tabInfo = null;
            if (curTabDataList != null && postion >= 0 && postion < curTabDataList.size()) {
                tabInfo = curTabDataList.get(postion);
            }
            WeakReference<Fragment> viewWeakReference;
            Fragment view = null;
            // 先从缓存cache里面取，如果取不到，new一个再存cache
            if (tabInfo != null) {
                viewWeakReference = viewCache.get(postion);
                if (viewWeakReference != null && viewWeakReference.get() != null) {
                    view = viewWeakReference.get();
                }
            }
            if (view == null && tabInfo != null) {
                // new一个view再存到cache
                view = getViewByType(tabInfo);
                if (view != null) {
                    viewCache.put(postion, new WeakReference<Fragment>(view));
                }
            }
            return view;
        }

        @Override
        public int getCount() {
            return curTabDataList.size();
        }

        private Fragment getViewByType(TopTabInfo tabInfo) {
            BaseFragment fragment = null;
            switch (tabInfo.getType()) {
                case TYPE_OBSERVATION:
                    fragment = new FinancingFragmentChild1();
                    break;
                case TYPE_INVESTMENT:
                    fragment = new FinancingFragmentChild2();
                    break;
                case TYPE_RECOMMENDATION:
                    fragment = new FinancingFragmentChild3();
                    break;
                default:
                    break;
            }
            return fragment;
        }
    }

    /**
     * viewpager的listener
     */
    public class OnTopTabPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int page) {
            mTopTab.onTabSelected(page, true);
            // 页面被切走的回调
            if (currIndex != page && mTabPagerAdapter.getItem(currIndex) != null) {
                ((BaseFragment) mTabPagerAdapter.getItem(currIndex)).onPageTurnBackground();
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mTopTab.onTabScrolled(position, positionOffset);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }
}
