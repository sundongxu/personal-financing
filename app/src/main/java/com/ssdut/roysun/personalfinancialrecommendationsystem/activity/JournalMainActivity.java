package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.User;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.anim.Animation3D;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.view.JournalMainAmountLeftView;
import com.ssdut.roysun.personalfinancialrecommendationsystem.component.view.JournalMainExpenditureIncomeView;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

import java.util.List;

/**
 * Created by roysun on 16/3/12.
 * 记账主页面
 */
public class JournalMainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalMainActivity";

    private TextView mExpenditureYearText, mExpenditureMonthText, mExpenditureDayText;
    private TextView mBudgetMonthText, mBudgetMonthLeftText;  // 支出界面的TextView
    private TextView mIncomeYearText, mIncomeMonthText, mIncomeDayText;  // 收入界面的TextView
    private FrameLayout mExpenditureTabArea, mIncomeTabArea;  // 顶部framelayout支出和收入
    private RelativeLayout mExpenditureStatsPic, mBudgetStatsPic, mIncomeStatsPic;  // 绘图区域
    private Button mBtnAdd, mBtnDetail, mBtnSheet, mBtnBudget, mBtnSettings;  // 底部环形按钮 添加、明细、报表、预算、设置
    private ImageView mExpenditureTabPic, mIncomeTabPic, mPopMenu;  // tab1顶部支出和收入按钮选中后背景图像
    private LinearLayout mJournalExpenditureArea, mJournalIncomeArea;  // tab1顶部支出和收入按钮选中后下面显示的内容
    private Button mBtns[];  //button集合 方便管理按钮显示隐藏
    private boolean mIsShown;  //底部环形按钮是否显示

    private JournalManager mJournalManager;  //数据库操作
    private User mCurUser;  // 当前用户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_main);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mIsShown = false;
        mJournalManager = JournalManager.getInstance(this);
        mCurUser = mUserManager.getCurUser();
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.title_journal_main_page);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mExpenditureTabPic = (ImageView) findViewById(R.id.iv_expenditure_tab_pic);
        mExpenditureTabPic.setImageResource(R.drawable.journal_main_tab_pic);
        mIncomeTabPic = (ImageView) findViewById(R.id.iv_income_tab_pic);
        mJournalExpenditureArea = (LinearLayout) findViewById(R.id.ll_journal_expenditure_main);
        mJournalExpenditureArea.setVisibility(View.VISIBLE);
        mJournalIncomeArea = (LinearLayout) findViewById(R.id.ll_journal_income_main);
        mJournalIncomeArea.setVisibility(View.GONE);
        mExpenditureStatsPic = (RelativeLayout) findViewById(R.id.rl_expenditure_stats_pic);
        mBudgetStatsPic = (RelativeLayout) findViewById(R.id.rl_budget_stats_pic);
        mIncomeStatsPic = (RelativeLayout) findViewById(R.id.rl_income_stats_pic);
        mExpenditureYearText = (TextView) findViewById(R.id.tv_expenditure_year);
        mExpenditureMonthText = (TextView) findViewById(R.id.tv_expenditure_month);
        mExpenditureDayText = (TextView) findViewById(R.id.tv_expenditure_day);
        mBudgetMonthText = (TextView) findViewById(R.id.tv_budget_month);
        mBudgetMonthLeftText = (TextView) findViewById(R.id.tv_budget_left_month);
        mIncomeYearText = (TextView) findViewById(R.id.tv_income_year);
        mIncomeMonthText = (TextView) findViewById(R.id.tv_income_month);
        mIncomeDayText = (TextView) findViewById(R.id.tv_income_day);

        //顶部收入、支出按钮
        mExpenditureTabArea = (FrameLayout) findViewById(R.id.fl_expenditure_tab);
        mExpenditureTabArea.setOnClickListener(this);
        mIncomeTabArea = (FrameLayout) findViewById(R.id.fl_income_tab);
        mIncomeTabArea.setOnClickListener(this);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);
        mBtnDetail = (Button) findViewById(R.id.btn_detail);
        mBtnDetail.setOnClickListener(this);
        mBtnSheet = (Button) findViewById(R.id.btn_sheet);
        mBtnSheet.setOnClickListener(this);
        mBtnBudget = (Button) findViewById(R.id.btn_budget);
        mBtnBudget.setOnClickListener(this);
        mBtnSettings = (Button) findViewById(R.id.btn_settings);
        mBtnSettings.setOnClickListener(this);
        mPopMenu = (ImageView) findViewById(R.id.iv_menu);
        mPopMenu.setOnClickListener(this);
        mBtns = new Button[]{mBtnAdd, mBtnDetail, mBtnSheet, mBtnBudget, mBtnSettings};
        hideMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mExpenditureStatsPic.removeAllViews();
        mBudgetStatsPic.removeAllViews();
        mIncomeStatsPic.removeAllViews();
        getExpenditure();
        getBudget();
        getIncome();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideMenu();
        mPopMenu.setImageResource(R.drawable.journal_main_pop_menu);
    }

    // 动画弹出底部菜单
    public void menuPopUpWithAnimation() {
        mIsShown = true;
        for (Button btn : mBtns) {
            btn.setAnimation(AnimationUtils.loadAnimation(this, R.anim.journal_main_menu_appear));
            btn.setVisibility(View.VISIBLE);
        }
    }

    // 动画隐藏底部环形菜单
    public void hideMenuWithAnimation() {
        mIsShown = false;
        for (Button btn : mBtns) {
            btn.setAnimation(AnimationUtils.loadAnimation(this, R.anim.journal_main_menu_disappear));
            btn.setVisibility(View.INVISIBLE);
        }
    }

    // 无动画隐藏底部环形菜单
    private void hideMenu() {
        mIsShown = false;
        for (Button btn : mBtns) {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_expenditure_tab:  // 顶部支出按钮
                mExpenditureTabPic.setImageResource(R.drawable.journal_main_tab_pic);
                mIncomeTabPic.setImageDrawable(null);
                mExpenditureTabPic.setAnimation(AnimationUtils.loadAnimation(this, R.anim.journal_main_top_right2left));
                Animation3D.delayShow(mJournalExpenditureArea, mJournalIncomeArea);
                break;
            case R.id.fl_income_tab:  // 顶部收入按钮
                mIncomeTabPic.setImageResource(R.drawable.journal_main_tab_pic);
                mExpenditureTabPic.setImageDrawable(null);
                mIncomeTabPic.setAnimation(AnimationUtils.loadAnimation(this, R.anim.journal_main_top_left2right));
                Animation3D.delayShow(mJournalIncomeArea, mJournalExpenditureArea);
                break;
            case R.id.iv_menu:  // 底部环形菜单
                if (!mIsShown) {
                    menuPopUpWithAnimation();
                    mPopMenu.setImageResource(R.drawable.journal_main_withdraw_menu);
                } else {
                    hideMenuWithAnimation();
                    mPopMenu.setImageResource(R.drawable.journal_main_pop_menu);
                }
                break;
            case R.id.btn_add:  // 记账
                changeActivity(JournalAddActivity.class);
                break;
            case R.id.btn_detail:  // 明细
                changeActivity(JournalDetailActivity.class);
                break;
            case R.id.btn_budget:  // 预算
                changeActivity(BudgetActivity.class);
                break;
            case R.id.btn_sheet:  // 报表
                changeActivity(JournalSheetActivity.class);
                break;
            case R.id.btn_settings:  // 设置
                changeActivity(JournalSettingActivity.class);
                break;
        }
    }

    public void changeActivity(Class<?> c) {
        Intent intent = new Intent(JournalMainActivity.this, c);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsShown) {
                    hideMenuWithAnimation();
                    mPopMenu.setImageResource(R.drawable.journal_main_pop_menu);
                    return false;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                if (mIsShown) {
                    hideMenuWithAnimation();
                    mPopMenu.setImageResource(R.drawable.journal_main_pop_menu);
                } else {
                    menuPopUpWithAnimation();
                    mPopMenu.setImageResource(R.drawable.journal_main_withdraw_menu);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 按年、月、日获取支出数据
    public void getExpenditure() {
        float fExpenditureYearSum = 0, fExpenditureMonthSum = 0, fExpenditureDaySum = 0;
        // 获取年支出
        String strSelectionExpenditureYear = Expenditure.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Expenditure.YEAR + "=" + TimeUtils.getYear();
        List<Expenditure> expenditureYearList = mJournalManager.getExpenditureListFromDB(strSelectionExpenditureYear);
        if (expenditureYearList != null) {
            for (Expenditure _expenditure : expenditureYearList) {
                fExpenditureYearSum += _expenditure.getAmount();
            }
            mExpenditureYearText.setText(fExpenditureYearSum + "");
        } else {
            mExpenditureYearText.setText(0 + "");
        }
        // 获取月支出
        String strSelectionExpenditureMonth = Expenditure.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + TimeUtils.getMonth();
        List<Expenditure> expenditureMonthList = mJournalManager.getExpenditureListFromDB(strSelectionExpenditureMonth);
        if (expenditureMonthList != null) {
            for (Expenditure expenditure : expenditureMonthList) {
                fExpenditureMonthSum += expenditure.getAmount();
            }
            mExpenditureMonthText.setText(fExpenditureMonthSum + "");
        } else {
            mExpenditureMonthText.setText(0 + "");
        }
        // 获取日支出
        String strSelectionExpenditureDay = Expenditure.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + TimeUtils.getMonth() + " and " + Expenditure.DAY + "=" + TimeUtils.getDay();
        List<Expenditure> expenditureDayList = mJournalManager.getExpenditureListFromDB(strSelectionExpenditureDay);
        if (expenditureDayList != null) {
            for (Expenditure expenditure : expenditureDayList) {
                fExpenditureDaySum += expenditure.getAmount();
            }
            mExpenditureDayText.setText(fExpenditureDaySum + "");
        } else {
            mExpenditureDayText.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if (fExpenditureYearSum > 0 || fExpenditureMonthSum > 0 || fExpenditureDaySum > 0) {
            // 创建绘图区域
            mExpenditureStatsPic.setBackgroundDrawable(null);
            System.out.println("sdx---r2=" + fExpenditureMonthSum / fExpenditureYearSum + "\n r3=" + fExpenditureDaySum / fExpenditureYearSum);
            mExpenditureStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.CYAN, 100, 1, "本年支出"));  // /20?
            mExpenditureStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.YELLOW, 250, fExpenditureMonthSum / fExpenditureYearSum, "本月支出"));
            mExpenditureStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.MAGENTA, 400, fExpenditureDaySum / fExpenditureYearSum, "今天支出"));
        } else {
            mExpenditureStatsPic.setBackgroundResource(R.drawable.journal_main_empty_expenditure);
        }
    }

    // 获取月预算数据
    public void getBudget() {
//        int iBudgetAmount = JournalSqliteHelper.readPreferenceFile(this, JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH);
        int iBudgetAmount = mCurUser.getBudget();
        mBudgetMonthText.setText(iBudgetAmount + "");  //月预算
        float fExpenditureMonth = Float.parseFloat(mExpenditureMonthText.getText().toString().trim());  //月预算余额 = 月预算 - 月支出
        mBudgetMonthLeftText.setText((iBudgetAmount - fExpenditureMonth) + "");

        int iStepping;  //根据不同的剩余调整步进速度
        if ((iBudgetAmount / fExpenditureMonth) < 0.3) {
            iStepping = 1;
        } else if ((iBudgetAmount / fExpenditureMonth) >= 0.3 && (iBudgetAmount / fExpenditureMonth) <= 0.6) {
            iStepping = 3;
        } else {
            iStepping = 6;
        }
        if (iBudgetAmount > 0 || fExpenditureMonth > 0) {
            mBudgetStatsPic.setBackgroundDrawable(null);
            mBudgetStatsPic.addView(new JournalMainAmountLeftView(this, 0, 0, Color.BLUE, 50));
            mBudgetStatsPic.addView(new JournalMainAmountLeftView(JournalMainActivity.this, iBudgetAmount, fExpenditureMonth, Color.RED, iStepping));
        } else {
            mBudgetStatsPic.setBackgroundResource(R.drawable.journal_main_empty_budget);
        }
    }

    // 按年、月、日获取收入数据
    public void getIncome() {
        float fIncomeYearSum = 0, fIncomeMonthSum = 0, fIncomeDaySum = 0;
        //获取年收入
        String strSelectionIncomeYear = Income.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Income.YEAR + "=" + TimeUtils.getYear();
        List<Income> incomeYearList = mJournalManager.getIncomeListFromDB(strSelectionIncomeYear);
        if (incomeYearList != null) {
            fIncomeYearSum = 0;
            for (Income income : incomeYearList) {
                fIncomeYearSum += income.getAmount();
            }
            mIncomeYearText.setText(fIncomeYearSum + "");
        } else {
            mIncomeYearText.setText(0 + "");
        }
        //获取月收入
        String strSelectionIncomeMonth = Income.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + TimeUtils.getMonth();
        List<Income> incomeMonthList = mJournalManager.getIncomeListFromDB(strSelectionIncomeMonth);
        if (incomeMonthList != null) {
            fIncomeMonthSum = 0;
            for (Income income : incomeMonthList) {
                fIncomeMonthSum += income.getAmount();
            }
            mIncomeMonthText.setText(fIncomeMonthSum + "");
        } else {
            mIncomeMonthText.setText(0 + "");
        }
        //获取日收入
        String strSelectionIncomeDay = Income.USER_NAME + "='" + mCurUser.getName() + "'" + " and " + Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + TimeUtils.getMonth() + " and " + Income.DAY + "=" + TimeUtils.getDay();
        List<Income> incomeDayList = mJournalManager.getIncomeListFromDB(strSelectionIncomeDay);
        if (incomeDayList != null) {
            fIncomeDaySum = 0;
            for (Income income : incomeDayList) {
                fIncomeDaySum += income.getAmount();
            }
            mIncomeDayText.setText(fIncomeDaySum + "");
        } else {
            mIncomeDayText.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if (fIncomeYearSum > 0 || fIncomeMonthSum > 0 || fIncomeDaySum > 0) {
            // 创建绘图区域
            mIncomeStatsPic.setBackgroundDrawable(null);
            System.out.println("sdx---r2=" + fIncomeMonthSum / fIncomeYearSum + "\n r3=" + fIncomeDaySum / fIncomeYearSum);
            mIncomeStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.CYAN, 100, 1, "本年收入"));
            mIncomeStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.YELLOW, 250, fIncomeMonthSum / fIncomeYearSum, "本月收入"));
            mIncomeStatsPic.addView(new JournalMainExpenditureIncomeView(this, Color.MAGENTA, 400, fIncomeDaySum / fIncomeYearSum, "今天收入"));
        } else {
            mIncomeStatsPic.setBackgroundResource(R.drawable.journal_main_empty_expenditure);
        }
    }
}
