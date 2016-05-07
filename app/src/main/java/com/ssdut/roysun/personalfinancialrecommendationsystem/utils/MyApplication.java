package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * Created by roysun on 16/4/27.
 * 集中管理Activity，解决system.exit(0)不能退出程序还重启Activity的问题
 * 调用appExit()方法成功解决，完全退出问题
 */
public class MyApplication extends Application {

    private static Stack<Activity> mActivityStack;
    private static MyApplication mSingleton;

    @Override
    public void onCreate() {
        super.onCreate();
        mSingleton = this;
    }

    //单例模式返回一个application示例
    public static MyApplication getInstance() {
        return mSingleton;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    /**
     * 输出堆栈全部元素，暂未实现
     */
    public static void printActivityStack() {
        for (Activity activity : mActivityStack) {

        }
    }

}
