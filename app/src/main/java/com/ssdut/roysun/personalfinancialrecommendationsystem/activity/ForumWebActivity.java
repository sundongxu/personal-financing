package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

public class ForumWebActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private WebPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_web);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
    }

    @Override
    protected void initView() {
//        super.initView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("最生活");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTabLayout = (TabLayout) findViewById(R.id.tl_consumation);
        mViewPager = (ViewPager) findViewById(R.id.vp_consumation);

        mViewPager.addOnPageChangeListener(this);
        setupViewPager(mViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText("第一页"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第二页"));
        mTabLayout.addTab(mTabLayout.newTab().setText("第三页"));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager mViewPager) {
        mPagerAdapter = new WebPagerAdapter(getSupportFragmentManager());
        TestFragment myFragment_1 = TestFragment.newInstance();
        TestFragment myFragment_2 = TestFragment.newInstance();
        TestFragment myFragment_3 = TestFragment.newInstance();
        mPagerAdapter.addFragment(myFragment_1, "第一页");
        mPagerAdapter.addFragment(myFragment_2, "第二页");
        mPagerAdapter.addFragment(myFragment_3, "第三页");
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);  //设置缓存的页面个数，简单粗暴，一次性加载最多5个tab的Fragment（即一次性执行最多5个tab的Fragment的onCreateView方法）
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mInputMethodManager.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
                break;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class WebPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public WebPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }
}
