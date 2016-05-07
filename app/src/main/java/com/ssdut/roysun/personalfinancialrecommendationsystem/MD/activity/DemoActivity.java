package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity;

import android.animation.Animator;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.fragment.OthersFragmentMD;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.BaseActivity;

import java.util.ArrayList;

public class DemoActivity extends BaseActivity {

    private OthersFragmentMD mCurrentFragment;
    private ArrayList<AHBottomNavigationItem> mBottomNavigationItems = new ArrayList<>();
    private FragmentManager mFragmentManager = getFragmentManager();
    private AHBottomNavigation mBottomNavigation;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
    }

    @Override
    protected void initView() {
//		super.initView();
        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_apps_black_24dp, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_maps_local_bar, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_maps_local_restaurant, R.color.color_tab_3);

        mBottomNavigationItems.add(item1);
        mBottomNavigationItems.add(item2);
        mBottomNavigationItems.add(item3);

        mBottomNavigation.addItems(mBottomNavigationItems);
        mBottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        mBottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        mBottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if (position == 1) {
                    mBottomNavigation.setNotification(0, 1);

                    if (!wasSelected) {
                        mFloatingActionButton.setVisibility(View.VISIBLE);
                        mFloatingActionButton.setAlpha(0f);
                        mFloatingActionButton.setScaleX(0f);
                        mFloatingActionButton.setScaleY(0f);
                        mFloatingActionButton.animate()
                                .alpha(1)
                                .scaleX(1)
                                .scaleY(1)
                                .setDuration(300)
                                .setInterpolator(new OvershootInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mFloatingActionButton.animate()
                                                .setInterpolator(new LinearOutSlowInInterpolator())
                                                .start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }
                } else {
                    if (mFloatingActionButton.getVisibility() == View.VISIBLE) {
                        mFloatingActionButton.animate()
                                .alpha(0)
                                .scaleX(0)
                                .scaleY(0)
                                .setDuration(300)
                                .setInterpolator(new LinearOutSlowInInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mFloatingActionButton.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        mFloatingActionButton.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }
                }

                if (!wasSelected) {
                    mCurrentFragment = OthersFragmentMD.newInstance(position);
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.fragment_container, mCurrentFragment)
                            .commit();
                } else if (position > 0) {
                    mCurrentFragment.refresh();
                }
            }
        });

        mCurrentFragment = OthersFragmentMD.newInstance(0);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomNavigation.setNotification(22, 1);
                Snackbar.make(mBottomNavigation, "Snackbar with bottom navigation",
                        Snackbar.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    /**
     * Update the bottom navigation colored param
     */
    public void updateBottomNavigationColor(boolean isColored) {
        mBottomNavigation.setColored(isColored);
    }

    /**
     * Return if the bottom navigation is colored
     */
    public boolean isBottomNavigationColored() {
        return mBottomNavigation.isColored();
    }

    /**
     * Add or remove items of the bottom navigation
     */
    public void updateBottomNavigationItems(boolean addItems) {

        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.tab_4),
                ContextCompat.getDrawable(this, R.drawable.ic_maps_local_bar),
                ContextCompat.getColor(this, R.color.color_tab_4));
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.tab_5),
                ContextCompat.getDrawable(this, R.drawable.ic_maps_place),
                ContextCompat.getColor(this, R.color.color_tab_5));

        if (addItems) {
            mBottomNavigation.addItem(item4);
            mBottomNavigation.addItem(item5);
            mBottomNavigation.setNotification(1, 3);
        } else {
            mBottomNavigation.removeAllItems();
            mBottomNavigation.addItems(mBottomNavigationItems);
        }
    }

    /**
     * Return the number of items in the bottom navigation
     */
    public int getBottomNavigationNbItems() {
        return mBottomNavigation.getItemsCount();
    }

}
