package com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;

/**
 * Created by roysun on 16/4/1.
 * 基础Fragment
 */
public abstract class BaseFragment extends Fragment {
    //定义上下文环境
    public Context mContext;
    protected LayoutInflater layoutInflater;
    public View createdView = null;
    public boolean hasAlreadyOncreate = false;
    public boolean isFirstOnresume = true;
    public static final String KEY_PARAM_SELECTED_TAB_TYPE = "_STT";//给外部传入的选中位置提供一个通用的变量（给内部还是viewpager的提供默认选中位置）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasAlreadyOncreate = true;
    }

    public boolean isAlreadyOncreate() {
        return hasAlreadyOncreate;
    }

    public BaseFragment(Activity context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(this.mContext);
    }

    public BaseFragment() {
        mContext = MainActivity.getInstance();
        layoutInflater = LayoutInflater.from(this.mContext);
    }

    /**
     * 设置页面的布局资源id
     *
     * @param resId
     */
    public void setContentView(int resId) {
        createdView = layoutInflater.inflate(resId, null, false);//这里root就无法传了，因为这个不inflate，后续find方法则无法工作
    }

    @Override
    public View getView() {
        View view = super.getView();
        if (view == null) {
            view = createdView;
        }
        return view;
    }

    /**
     * 通过资源id获得页面中的组件
     *
     * @param resId
     * @return
     */
    public View findViewById(int resId) {
        if (createdView != null) {
            return createdView.findViewById(resId);
        }
        return null;
    }

    public void setContentView(View view) {
        this.createdView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (createdView == null) {
            createdView = new View(mContext);
        }
        ViewParent viewParent = createdView.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(createdView);
        }
        return createdView;
    }

    /**
     * @param isSnapResume true表示滑动产生的onPageResume ，false表示activity切换导致的onPageResume
     */
    public void onPageResume(boolean isSnapResume) {

    }

    /**
     * 当页面被选中的时候调用
     * 可以在页面选中的方法中实现数据的获取和填充
     */

    @Override
    final public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 当页面切换走时被调用
     */
    public void onPageTurnBackground() {

    }

    /**
     * 重写该方法可控制当前只加载可见Fragment
     * @param isVisibleToUser 当前Fragment对用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onPageResume(true);  //Fragment可见时加载数据，这里只有ViewPager做Fragment切换才会调用，故参数为true，表明是滑动而不是切换Activity
            isFirstOnresume = false;
        }

    }
}
