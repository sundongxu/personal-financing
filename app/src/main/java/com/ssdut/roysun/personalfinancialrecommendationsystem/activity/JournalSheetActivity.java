package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Expenditure;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Income;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.JournalItem;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.JournalManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roysun on 16/3/12.
 * 报表页面
 */
public class JournalSheetActivity extends BaseActivity {

    public static final String TAG = "JournalSheetActivity";

    public static final int EXPENDITURE_INCOME = 1111;
    public static final int EXPENDITURE_DETAIL = 2222;
    public static final int INCOME_DETAIL = 3333;

    private JournalManager mJournalManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView(EXPENDITURE_INCOME));
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mJournalManager = JournalManager.getInstance(this);
    }

    // 设置走线图
    public View setView(int flag) {
        String[] titles = null;  // 收支条目 大类
        int[] colors = null;
        PointStyle[] styles = null;
        List<double[]> values = null;
        String title = "";
        switch (flag) {
            case EXPENDITURE_INCOME:
                titles = new String[]{"月收入", "月支出"};
                colors = new int[]{Color.WHITE, Color.GREEN};
                styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND};
                values = new ArrayList<double[]>();
                values.add(getIncomeThisYearMonth(""));
                values.add(getExpenditureThisYearMonth(""));
                title = "月收入和支出走势图";
                break;
            case EXPENDITURE_DETAIL:
                titles = new String[]{JournalItem.MEAL, JournalItem.TRAFFIC, JournalItem.SHOPPING, JournalItem.ENTERTAINMENT, JournalItem.MEDITATION_EDUCATION, JournalItem.DAILY_EXPENSE, JournalItem.INVESTMENT, JournalItem.FAVOR_CONTACT, JournalItem.LENDING, JournalItem.PAYMENT};
                colors = new int[]{Color.WHITE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.MAGENTA, Color.RED, Color.BLUE, Color.LTGRAY, Color.GRAY};
                styles = new PointStyle[]{PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND};
                values = new ArrayList<double[]>();
                String selectionMeal = " and " + Expenditure.CATEGORY + " = '" + JournalItem.MEAL + "'";
                values.add(getExpenditureThisYearMonth(selectionMeal));
                String selectTraffic = " and " + Expenditure.CATEGORY + " = '" + JournalItem.TRAFFIC + "'";
                values.add(getExpenditureThisYearMonth(selectTraffic));
                String selectionShopping = " and " + Expenditure.CATEGORY + " = '" + JournalItem.SHOPPING + "'";
                values.add(getExpenditureThisYearMonth(selectionShopping));
                String selectionEntertainment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.ENTERTAINMENT + "'";
                values.add(getExpenditureThisYearMonth(selectionEntertainment));
                String selectionMeditationEducation = " and " + Expenditure.CATEGORY + " = '" + JournalItem.MEDITATION_EDUCATION + "'";
                values.add(getExpenditureThisYearMonth(selectionMeditationEducation));
                String selectionDailyExpense = " and " + Expenditure.CATEGORY + " = '" + JournalItem.DAILY_EXPENSE + "'";
                values.add(getExpenditureThisYearMonth(selectionDailyExpense));
                String selectionInvestment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.INVESTMENT + "'";
                values.add(getExpenditureThisYearMonth(selectionInvestment));
                String selectionFavorContact = " and " + Expenditure.CATEGORY + " = '" + JournalItem.FAVOR_CONTACT + "'";
                values.add(getExpenditureThisYearMonth(selectionFavorContact));
                String selectionLend = " and " + Expenditure.CATEGORY + " = '" + JournalItem.LENDING + "'";
                values.add(getExpenditureThisYearMonth(selectionLend));
                String selectionPayment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.PAYMENT + "'";
                values.add(getExpenditureThisYearMonth(selectionPayment));
                title = "支出走势图";
                break;
            case INCOME_DETAIL:
                titles = new String[]{JournalItem.SALARY, JournalItem.STOCK, JournalItem.BONUS, JournalItem.INTERESTS, JournalItem.DIVIDEND, JournalItem.SUBSIDY, JournalItem.REIMBURSEMENT, JournalItem.OTHERS, JournalItem.BORROWING, JournalItem.RECEIVEMENT};
                colors = new int[]{Color.WHITE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.MAGENTA, Color.RED, Color.BLUE, Color.LTGRAY, Color.GRAY};
                styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE};
                values = new ArrayList<double[]>();
                String selectionSalary = " and " + Income.CATEGORY + " = '" + JournalItem.SALARY + "'";
                values.add(getIncomeThisYearMonth(selectionSalary));
                String selectionStock = " and " + Income.CATEGORY + " = '" + JournalItem.STOCK + "'";
                values.add(getIncomeThisYearMonth(selectionStock));
                String selectionBonus = " and " + Income.CATEGORY + " = '" + JournalItem.BONUS + "'";
                values.add(getIncomeThisYearMonth(selectionBonus));
                String selectionInterests = " and " + Income.CATEGORY + " = '" + JournalItem.INTERESTS + "'";
                values.add(getIncomeThisYearMonth(selectionInterests));
                String selectionDividend = " and " + Income.CATEGORY + " = '" + JournalItem.DIVIDEND + "'";
                values.add(getIncomeThisYearMonth(selectionDividend));
                String selectionSubsidy = " and " + Income.CATEGORY + " = '" + JournalItem.SUBSIDY + "'";
                values.add(getIncomeThisYearMonth(selectionSubsidy));
                String selectionReimbursement = " and " + Income.CATEGORY + " = '" + JournalItem.REIMBURSEMENT + "'";
                values.add(getIncomeThisYearMonth(selectionReimbursement));
                String selectionOthers = " and " + Income.CATEGORY + " = '" + JournalItem.OTHERS + "'";
                values.add(getIncomeThisYearMonth(selectionOthers));
                String selectionBorrow = " and " + Income.CATEGORY + " = '" + JournalItem.BORROWING + "'";
                values.add(getIncomeThisYearMonth(selectionBorrow));
                String selectionReceivement = " and " + Income.CATEGORY + " = '" + JournalItem.RECEIVEMENT + "'";
                values.add(getIncomeThisYearMonth(selectionReceivement));
                title = "收入走势图";
                break;
        }
        List<double[]> list = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            list.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }

        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, title, "月份", "金额", 1, 12, 0, 10000, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Paint.Align.RIGHT);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[]{0, 12, 0, 10000});
        renderer.setZoomLimits(new double[]{0, 12, 0, 10000});
        View view = ChartFactory.getLineChartView(this, buildDataset(titles, list, values), renderer);
        view.setBackgroundColor(Color.BLACK);
        return view;
    }

    // 获取年、月支出数据
    public double[] getExpenditureThisYearMonth(String selection) {
        double d[] = new double[12];
        ArrayList<Expenditure> expenditureThisYearList;
        for (int i = 1; i <= 12; i++) {
            double expenditureSum = 0;
            String selectionExpenditure = Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + i + selection;
            expenditureThisYearList = mJournalManager.getExpenditureListFromDB(selectionExpenditure);
            for (Expenditure expenditure : expenditureThisYearList) {
                expenditureSum += expenditure.getAmount();
            }
            d[i - 1] = expenditureSum;
        }
        return d;
    }

    // 获取年、月收入数据
    public double[] getIncomeThisYearMonth(String selection) {
        double d[] = new double[12];
        ArrayList<Income> incomeThisYearList;
        for (int i = 1; i <= 12; i++) {
            double incomeSum = 0;
            String selectionIncome = Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + i + selection;
            incomeThisYearList = mJournalManager.getIncomeListFromDB(selectionIncome);
            for (Income income : incomeThisYearList) {
                incomeSum += income.getAmount();
            }
            d[i - 1] = incomeSum;
        }
        return d;
    }

    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{20, 30, 15, 20});
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 100, 0, "月收入和支出");
        menu.add(0, 200, 0, "详细月收入");
        menu.add(0, 300, 0, "详细月支出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 100:
                setContentView(setView(EXPENDITURE_INCOME));
                break;
            case 200:
                setContentView(setView(INCOME_DETAIL));
                break;
            case 300:
                setContentView(setView(EXPENDITURE_DETAIL));
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
