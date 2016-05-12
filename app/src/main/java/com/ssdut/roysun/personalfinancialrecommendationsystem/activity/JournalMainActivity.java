package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.JournalSqliteHelper;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.DongHua3d;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.JZPaintViewYuE;
import com.ssdut.roysun.personalfinancialrecommendationsystem.service.JZPaintViewZandS;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.DialogUtils;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

import java.util.List;

/**
 * Created by roysun on 16/3/12.
 * 记账主页面
 */
public class JournalMainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "JournalMainActivity";

    private TextView mExpenditureYear, mExpenditureMonth, mExpenditureDay;
    private TextView mBudgetMonth, mBudgetMonthLeft;  //支出界面的TextView
    private TextView mIncomeYear, mIncomeMonth, mIncomeDay;  //收入界面的TextView
    private FrameLayout mExpenditureTabArea, mIncomeTabArea;  //顶部framelayout支出和收入
    private RelativeLayout mExpenditureStatsPic, mBudgetStatsPic, mIncomeStatsPic;  //绘图区域
    private Button mBtnAdd, mBtnDetail, mBtnSheet, mBtnBudget, mBtnSettings;  //底部环形按钮 添加， 明细，报表，预算，设置。
    private ImageView mExpenditureTabPic, mIncomeTabPic, mMenu;  //tab1顶部支出和收入按钮选中后背景图像
    private LinearLayout mJournalExpenditureArea, mJournalIncomeArea;  //tab1顶部支出和收入按钮选中后下面显示的内容
    private Button mBtns[] = null;  //button集合 方便管理按钮显示隐藏
    private boolean isShown = false;  //底部环形按钮是否显示

    private JournalManager mJournalDataHelper;  //数据库操作

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
        mJournalDataHelper = new JournalManager(this);
    }

    @Override
    protected void initView() {
        mExpenditureTabPic = (ImageView) findViewById(R.id.iv_expenditure_tab_pic);
        mExpenditureTabPic.setImageResource(R.drawable.jz_tab1_bt_bgs);
        mIncomeTabPic = (ImageView) findViewById(R.id.iv_income_tab_pic);
        mJournalExpenditureArea = (LinearLayout) findViewById(R.id.ll_journal_expenditure_main);
        mJournalExpenditureArea.setVisibility(View.VISIBLE);
        mJournalIncomeArea = (LinearLayout) findViewById(R.id.ll_journal_income_main);
        mJournalIncomeArea.setVisibility(View.GONE);
        mExpenditureStatsPic = (RelativeLayout) findViewById(R.id.rl_expenditure_stats_pic);
        mBudgetStatsPic = (RelativeLayout) findViewById(R.id.rl_budget_stats_pic);
        mIncomeStatsPic = (RelativeLayout) findViewById(R.id.rl_income_stats_pic);
        mExpenditureYear = (TextView) findViewById(R.id.tv_expenditure_year);
        mExpenditureMonth = (TextView) findViewById(R.id.tv_expenditure_month);
        mExpenditureDay = (TextView) findViewById(R.id.tv_expenditure_day);
        mBudgetMonth = (TextView) findViewById(R.id.tv_budget_month);
        mBudgetMonthLeft = (TextView) findViewById(R.id.tv_budget_left_month);
        mIncomeYear = (TextView) findViewById(R.id.tv_income_year);
        mIncomeMonth = (TextView) findViewById(R.id.tv_income_month);
        mIncomeDay = (TextView) findViewById(R.id.tv_income_day);

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
        mMenu = (ImageView) findViewById(R.id.iv_menu);
        mMenu.setOnClickListener(this);
        mBtns = new Button[]{mBtnAdd, mBtnDetail, mBtnSheet, mBtnBudget, mBtnSettings};
        hideView();
    }

    @Override
    protected void onResume() {
        mExpenditureStatsPic.removeAllViews();
        mBudgetStatsPic.removeAllViews();
        mIncomeStatsPic.removeAllViews();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        getExpenditure();
        getBudget();
        getIncome();
        super.onResume();
    }

    @Override
    protected void onPause() {
        hideView();
        mMenu.setImageResource(R.drawable.jz_main_more);
        super.onPause();
    }

    /*
     * 底部环形按钮显示动画
     */
    public void btnDongHua() {
        isShown = true;
        for (Button btn : mBtns) {
            dongHua(btn);
        }
    }

    public void dongHua(Button b) {
        b.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_up));
        b.setVisibility(View.VISIBLE);
    }

    /*
     * 隐藏底部环形按钮动画
     */
    public void btnHiddenDonghua() {
        isShown = false;
        final Handler handler = new Handler();
        for (Button bt : mBtns) {
            bt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_menu_down));
        }
        new Thread() {
            public void run() {
                try {
                    sleep(300);
                    handler.post(new Runnable() {
                        public void run() {
                            hideView();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*
     * 隐藏底部环形按钮
     */
    private void hideView() {
        isShown = false;
        for (Button btn : mBtns) {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_expenditure_tab:  //顶部支出按钮
                mExpenditureTabPic.setImageResource(R.drawable.jz_tab1_bt_bgs);
                mIncomeTabPic.setImageDrawable(null);
                mExpenditureTabPic.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_right2left));
                DongHua3d.yanChiShow(mJournalExpenditureArea, mJournalIncomeArea);
                break;
            case R.id.fl_income_tab:  //顶部收入按钮
                mIncomeTabPic.setImageResource(R.drawable.jz_tab1_bt_bgs);
                mExpenditureTabPic.setImageDrawable(null);
                mIncomeTabPic.setAnimation(AnimationUtils.loadAnimation(this, R.anim.jz_top_left2right));
                DongHua3d.yanChiShow(mJournalIncomeArea, mJournalExpenditureArea);
                break;
            case R.id.iv_menu:  //底部环形按钮
                if (!isShown) {
                    btnDongHua();
                    mMenu.setImageResource(R.drawable.jz_main_more_s);
                } else {
                    btnHiddenDonghua();
                    mMenu.setImageResource(R.drawable.jz_main_more);
                }
                break;
            case R.id.btn_add:  //记账
                changeActivity(JournalAddActivity.class);
                break;
            case R.id.btn_detail:  //明细
                changeActivity(JournalDetailActivity.class);
                break;
            case R.id.btn_budget:  //预算
                changeActivity(BudgetActivity.class);
                break;
            case R.id.btn_sheet:  //报表
                changeActivity(JournalSheetActivity.class);
                break;
            case R.id.btn_settings:  //设置
                changeActivity(JournalSettingActivity.class);
                break;
        }
    }

    /*
     * 切换界面
     */
    public void changeActivity(Class<?> c) {
        Intent intent = new Intent(JournalMainActivity.this, c);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isShown) {
                    btnHiddenDonghua();
                    mMenu.setImageResource(R.drawable.jz_main_more);
                    return false;
                } else {
                    DialogUtils.showExitDialog(this, ACTIVITY_JOURNAL_MAIN);
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                if (isShown) {
                    btnHiddenDonghua();
                    mMenu.setImageResource(R.drawable.jz_main_more);
                } else {
                    btnDongHua();
                    mMenu.setImageResource(R.drawable.jz_main_more_s);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取支出页面各个金额总数：年、月、日
     */
    public void getExpenditure() {
        float _expenditureYearSum = 0, _expenditureMonthSum = 0, _expenditureDaySum = 0;
        String _selectionExpenditureYear = Expenditure.YEAR + "=" + TimeUtils.getYear();
        List<Expenditure> _expenditureYearList = mJournalDataHelper.getExpenditureListFromDB(_selectionExpenditureYear);
        if (_expenditureYearList != null) {
            for (Expenditure _expenditure : _expenditureYearList) {
                _expenditureYearSum += _expenditure.getAmount();
            }
            mExpenditureYear.setText(_expenditureYearSum + "");
        } else {
            mExpenditureYear.setText(0 + "");
        }

        String _selectionExpenditureMonth = Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + TimeUtils.getMonth();
        List<Expenditure> _expenditureMonthList = mJournalDataHelper.getExpenditureListFromDB(_selectionExpenditureMonth);
        if (_expenditureMonthList != null) {
            for (Expenditure _expenditure : _expenditureMonthList) {
                _expenditureMonthSum += _expenditure.getAmount();
            }
            mExpenditureMonth.setText(_expenditureMonthSum + "");
        } else {
            mExpenditureMonth.setText(0 + "");
        }

        String _selectionExpenditureDay = Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + TimeUtils.getMonth() + " and " + Expenditure.DAY + "=" + TimeUtils.getDay();
        List<Expenditure> _expenditureDayList = mJournalDataHelper.getExpenditureListFromDB(_selectionExpenditureDay);
        if (_expenditureDayList != null) {
            for (Expenditure _expenditure : _expenditureDayList) {
                _expenditureDaySum += _expenditure.getAmount();
            }
            mExpenditureDay.setText(_expenditureDaySum + "");
        } else {
            mExpenditureDay.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if (_expenditureYearSum > 0 || _expenditureMonthSum > 0 || _expenditureDaySum > 0) {
            // 创建绘图区域
            mExpenditureStatsPic.setBackgroundDrawable(null);
            mExpenditureStatsPic.addView(new JZPaintViewZandS(this, Color.BLUE, 30, _expenditureYearSum / 20, "本年支出"));  // /20?
            mExpenditureStatsPic.addView(new JZPaintViewZandS(this, Color.BLACK, 100, _expenditureMonthSum / 20, "本月支出"));
            mExpenditureStatsPic.addView(new JZPaintViewZandS(this, Color.CYAN, 170, _expenditureDaySum / 20, "今天支出"));

        } else {
            mExpenditureStatsPic.setBackgroundResource(R.drawable.jz_empty_zhichu_zhuxing);
        }
    }

    /**
     * 获取预算
     */
    public void getBudget() {
        final float _budgetAmount = JournalSqliteHelper.readPreferenceFile(this, JournalSqliteHelper.BUDGET_MONTH, JournalSqliteHelper.BUDGET_MONTH);
        mBudgetMonth.setText(_budgetAmount + "");  //月预算
        final float _expenditureMonth = Float.parseFloat(mExpenditureMonth.getText().toString().trim());  //月预算余额 = 月预算 - 月支出
        mBudgetMonthLeft.setText((_budgetAmount - _expenditureMonth) + "");

        int _stepping;  //根据不同的剩余调整步进速度
        if ((_budgetAmount / _expenditureMonth) < 0.3) {
            _stepping = 1;
        } else if ((_budgetAmount / _expenditureMonth) >= 0.3 && (_budgetAmount / _expenditureMonth) <= 0.6) {
            _stepping = 3;
        } else {
            _stepping = 6;
        }
        if (_budgetAmount > 0 || _expenditureMonth > 0) {
            mBudgetStatsPic.setBackgroundDrawable(null);
            mBudgetStatsPic.addView(new JZPaintViewYuE(this, 0, 0, Color.BLUE, 50));
            mBudgetStatsPic.addView(new JZPaintViewYuE(JournalMainActivity.this, _budgetAmount, _expenditureMonth, Color.RED, _stepping));
        } else {
            mBudgetStatsPic.setBackgroundResource(R.drawable.jz_empty_yusuan);
        }
    }

    /*
     * 获取收入页面各个金额总数：年、月、日
     */
    public void getIncome() {
        float _incomeYearSum = 0, _incomeMonthSum = 0, _incomeDaySum = 0;
        //获取年收入
        String _selectionIncomeYear = Income.YEAR + "=" + TimeUtils.getYear();
        List<Income> _incomeYearList = mJournalDataHelper.getIncomeListFromDB(_selectionIncomeYear);
        if (_incomeYearList != null) {
            _incomeYearSum = 0;
            for (Income _income : _incomeYearList) {
                _incomeYearSum += _income.getAmount();
            }
            mIncomeYear.setText(_incomeYearSum + "");
        } else {
            mIncomeYear.setText(0 + "");
        }
        //获取月收入
        String _selectionIncomeMonth = Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + TimeUtils.getMonth();
        List<Income> _incomeMonthList = mJournalDataHelper.getIncomeListFromDB(_selectionIncomeMonth);
        if (_incomeMonthList != null) {
            _incomeMonthSum = 0;
            for (Income _income : _incomeMonthList) {
                _incomeMonthSum += _income.getAmount();
            }
            mIncomeMonth.setText(_incomeMonthSum + "");
        } else {
            mIncomeMonth.setText(0 + "");
        }
        //获取日收入
        String _selectionIncomeDay = Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + TimeUtils.getMonth() + " and " + Income.DAY + "=" + TimeUtils.getDay();
        List<Income> _incomeDayList = mJournalDataHelper.getIncomeListFromDB(_selectionIncomeDay);
        if (_incomeDayList != null) {
            _incomeDaySum = 0;
            for (Income _income : _incomeDayList) {
                _incomeDaySum += _income.getAmount();
            }
            mIncomeDay.setText(_incomeDaySum + "");
        } else {
            mIncomeDay.setText(0 + "");
        }
        //判断当前状态确定是否绘图
        if (_incomeYearSum > 0 || _incomeMonthSum > 0 || _incomeDaySum > 0) {
            // 创建绘图区域
            mIncomeStatsPic.setBackgroundDrawable(null);
            mIncomeStatsPic.addView(new JZPaintViewZandS(this, Color.BLUE, 30, _incomeYearSum / 40, "本年收入"));
            mIncomeStatsPic.addView(new JZPaintViewZandS(this, Color.BLACK, 100, _incomeMonthSum / 40, "本月收入"));
            mIncomeStatsPic.addView(new JZPaintViewZandS(this, Color.CYAN, 170, _incomeDaySum / 40, "今天收入"));
        } else {
            mIncomeStatsPic.setBackgroundResource(R.drawable.jz_empty_zhichu_zhuxing);
        }
    }
}
