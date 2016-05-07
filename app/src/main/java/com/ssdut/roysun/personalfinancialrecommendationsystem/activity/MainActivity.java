package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.AccountFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.FinancingFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.JournalFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.fragment.OthersFragment;
import com.ssdut.roysun.personalfinancialrecommendationsystem.callback.MainFragmentCallback;

/**
 * Created by roysun on 16/4/1.
 * 主Activity，底部tab+4个主Fragment，仅点击切换
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener, MainFragmentCallback {

    public static final String TAG = "MainActivity";

    public static MainActivity mInstance;

    private LinearLayout mJournalTabArea;
    private LinearLayout mFinancingTabArea;
    private LinearLayout mAccountTabArea;
    private LinearLayout mOthersTabArea;

    private ImageButton mIBJournal;
    private ImageButton mIBFinancing;
    private ImageButton mIBAccount;
    private ImageButton mIBOthers;

    private Fragment mJournalFragment;
    private Fragment mFinancingFragment;
    private Fragment mAccountFragment;
    private Fragment mOthersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInstance = this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setCurTab(0);
    }

    private void initEvent() {
        mJournalTabArea.setOnClickListener(this);
        mFinancingTabArea.setOnClickListener(this);
        mAccountTabArea.setOnClickListener(this);
        mOthersTabArea.setOnClickListener(this);
    }

    private void initView() {
        mJournalTabArea = (LinearLayout) findViewById(R.id.ll_tab_journal);
        mFinancingTabArea = (LinearLayout) findViewById(R.id.ll_tab_financing);
        mAccountTabArea = (LinearLayout) findViewById(R.id.ll_tab_account);
        mOthersTabArea = (LinearLayout) findViewById(R.id.ll_tab_others);

        mIBJournal = (ImageButton) findViewById(R.id.ib_tab_journal);
        mIBFinancing = (ImageButton) findViewById(R.id.ib_tab_financing);
        mIBAccount = (ImageButton) findViewById(R.id.ib_tab_account);
        mIBOthers = (ImageButton) findViewById(R.id.ib_tab_others);
    }

    private void setCurTab(int tabIndex){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (tabIndex) {
            case 0:
                if (mJournalFragment == null) {
                    mJournalFragment = new JournalFragment();
                    transaction.add(R.id.fl_content, mJournalFragment);
                } else {
                    transaction.show(mJournalFragment);
                }
                mIBJournal.setImageResource(R.drawable.tab_journal_chosen);
                break;
            case 1:
                if (mFinancingFragment == null) {
                    mFinancingFragment = new AccountFragment();
                    transaction.add(R.id.fl_content, mFinancingFragment);
                } else {
                    transaction.show(mFinancingFragment);

                }
                mIBFinancing.setImageResource(R.drawable.tab_financing_chosen);
                break;
            case 2:
                if (mAccountFragment == null) {
                    mAccountFragment = new FinancingFragment();
                    transaction.add(R.id.fl_content, mAccountFragment);
                } else {
                    transaction.show(mAccountFragment);
                }
                mIBAccount.setImageResource(R.drawable.tab_account_chosen);
                break;
            case 3:
                if (mOthersFragment == null) {
                    mOthersFragment = new OthersFragment();
                    transaction.add(R.id.fl_content, mOthersFragment);
                } else {
                    transaction.show(mOthersFragment);
                }
                mIBOthers.setImageResource(R.drawable.tab_others_chosen);
                break;

            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mJournalFragment != null) {
            transaction.hide(mJournalFragment);
        }
        if (mFinancingFragment != null) {
            transaction.hide(mFinancingFragment);
        }
        if (mAccountFragment != null) {
            transaction.hide(mAccountFragment);
        }
        if (mOthersFragment != null) {
            transaction.hide(mOthersFragment);
        }
    }

    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.ib_tab_journal:
                setCurTab(0);
                break;
            case R.id.ib_tab_financing:
                setCurTab(1);
                break;
            case R.id.ib_tab_account:
                setCurTab(2);
                break;
            case R.id.ib_tab_others:
                setCurTab(3);
                break;

            default:
                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        mIBJournal.setImageResource(R.drawable.tab_journal_normal);
        mIBFinancing.setImageResource(R.drawable.tab_financing_normal);
        mIBAccount.setImageResource(R.drawable.tab_account_normal);
        mIBOthers.setImageResource(R.drawable.tab_others_normal);
    }

    @Override
    public void onFragmentInteraction() {
        //Fragment与MainActivity交互接口
    }

    public static MainActivity getInstance() {
        return mInstance;
    }
}
