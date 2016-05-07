package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
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

    private JournalManager mJournalDataHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView(EXPENDITURE_INCOME));
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        mJournalDataHelper = new JournalManager(this);
    }

    /*
         * 设置走线图
         * */
    public View setView(int flag) {
        String[] _titles = null;  //收支条目 大类
        int[] _colors = null;
        PointStyle[] _styles = null;
        List<double[]> values = null;
        String _title = "";
        switch (flag) {
            case EXPENDITURE_INCOME:
                _titles = new String[]{"月收入", "月支出"};
                _colors = new int[]{Color.WHITE, Color.GREEN};
                _styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND};
                values = new ArrayList<double[]>();
                values.add(getIncomeThisYear(""));
                values.add(getExpenditureThisYear(""));
                _title = "月收入和支出走势图";
                break;
            case EXPENDITURE_DETAIL:
                _titles = new String[]{JournalItem.MEAL, JournalItem.TRAFFIC, JournalItem.SHOPPING, JournalItem.ENTERTAINMENT, JournalItem.MEDITATION_EDUCATION, JournalItem.DAILY_EXPENSE, JournalItem.INVESTMENT, JournalItem.FAVOR_CONTACT, JournalItem.LEND, JournalItem.PAYMENT};
                _colors = new int[]{Color.WHITE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.MAGENTA, Color.RED, Color.BLUE, Color.LTGRAY, Color.GRAY};
                _styles = new PointStyle[]{PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND};
                values = new ArrayList<double[]>();
                String _selectionMeal = " and " + Expenditure.CATEGORY + " = '" + JournalItem.MEAL + "'";
                values.add(getExpenditureThisYear(_selectionMeal));
                String _selectTraffic = " and " + Expenditure.CATEGORY + " = '" + JournalItem.TRAFFIC + "'";
                values.add(getExpenditureThisYear(_selectTraffic));
                String _selectionShopping = " and " + Expenditure.CATEGORY + " = '" + JournalItem.SHOPPING + "'";
                values.add(getExpenditureThisYear(_selectionShopping));
                String _selectionEntertainment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.ENTERTAINMENT + "'";
                values.add(getExpenditureThisYear(_selectionEntertainment));
                String _selectionMeditationEducation = " and " + Expenditure.CATEGORY + " = '" + JournalItem.MEDITATION_EDUCATION + "'";
                values.add(getExpenditureThisYear(_selectionMeditationEducation));
                String _selectionDailyExpense = " and " + Expenditure.CATEGORY + " = '" + JournalItem.DAILY_EXPENSE + "'";
                values.add(getExpenditureThisYear(_selectionDailyExpense));
                String _selectionInvestment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.INVESTMENT + "'";
                values.add(getExpenditureThisYear(_selectionInvestment));
                String _selectionFavorContact = " and " + Expenditure.CATEGORY + " = '" + JournalItem.FAVOR_CONTACT + "'";
                values.add(getExpenditureThisYear(_selectionFavorContact));
                String _selectionLend = " and " + Expenditure.CATEGORY + " = '" + JournalItem.LEND + "'";
                values.add(getExpenditureThisYear(_selectionLend));
                String _selectionPayment = " and " + Expenditure.CATEGORY + " = '" + JournalItem.PAYMENT + "'";
                values.add(getExpenditureThisYear(_selectionPayment));
                _title = "支出走势图";
                break;
            case INCOME_DETAIL:
                _titles = new String[]{JournalItem.SALARY, JournalItem.STOCK, JournalItem.BONUS, JournalItem.INTERESTS, JournalItem.DIVIDEND, JournalItem.SUBSIDY, JournalItem.REIMBURSEMENT, JournalItem.OTHERS, JournalItem.BORROW, JournalItem.RECEIVEMENT};
                _colors = new int[]{Color.WHITE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.DKGRAY, Color.MAGENTA, Color.RED, Color.BLUE, Color.LTGRAY, Color.GRAY};
                _styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE};
                values = new ArrayList<double[]>();
                String _selectionSalary = " and " + Income.CATEGORY + " = '" + JournalItem.SALARY + "'";
                values.add(getIncomeThisYear(_selectionSalary));
                String _selectionStock = " and " + Income.CATEGORY + " = '" + JournalItem.STOCK + "'";
                values.add(getIncomeThisYear(_selectionStock));
                String _selectionBonus = " and " + Income.CATEGORY + " = '" + JournalItem.BONUS + "'";
                values.add(getIncomeThisYear(_selectionBonus));
                String _selectionInterests = " and " + Income.CATEGORY + " = '" + JournalItem.INTERESTS + "'";
                values.add(getIncomeThisYear(_selectionInterests));
                String _selectionDividend = " and " + Income.CATEGORY + " = '" + JournalItem.DIVIDEND + "'";
                values.add(getIncomeThisYear(_selectionDividend));
                String _selectionSubsidy = " and " + Income.CATEGORY + " = '" + JournalItem.SUBSIDY + "'";
                values.add(getIncomeThisYear(_selectionSubsidy));
                String _selectionReimbursement = " and " + Income.CATEGORY + " = '" + JournalItem.REIMBURSEMENT + "'";
                values.add(getIncomeThisYear(_selectionReimbursement));
                String _selectionOthers = " and " + Income.CATEGORY + " = '" + JournalItem.OTHERS + "'";
                values.add(getIncomeThisYear(_selectionOthers));
                String _selectionBorrow = " and " + Income.CATEGORY + " = '" + JournalItem.BORROW + "'";
                values.add(getIncomeThisYear(_selectionBorrow));
                String _selectionReceivement = " and " + Income.CATEGORY + " = '" + JournalItem.RECEIVEMENT + "'";
                values.add(getIncomeThisYear(_selectionReceivement));
                _title = "收入走势图";
                break;
        }
        List<double[]> _list = new ArrayList<double[]>();
        for (int i = 0; i < _titles.length; i++) {
            _list.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }

        XYMultipleSeriesRenderer _renderer = buildRenderer(_colors, _styles);
        int length = _renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) _renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(_renderer, _title, "月份", "金额", 1, 12, 0, 10000, Color.LTGRAY, Color.LTGRAY);
        _renderer.setXLabels(12);
        _renderer.setYLabels(10);
        _renderer.setShowGrid(true);
        _renderer.setXLabelsAlign(Paint.Align.RIGHT);
        _renderer.setYLabelsAlign(Paint.Align.RIGHT);
        _renderer.setZoomButtonsVisible(true);
        _renderer.setPanLimits(new double[]{0, 12, 0, 10000});
        _renderer.setZoomLimits(new double[]{0, 12, 0, 10000});
        View view = ChartFactory.getLineChartView(this, buildDataset(_titles, _list, values), _renderer);
        view.setBackgroundColor(Color.BLACK);
        return view;
    }

    /*
     * 从数据库中获得当年每月的支出信息
     * */
    public double[] getExpenditureThisYear(String selection) {
        double _d[] = new double[12];
        ArrayList<Expenditure> _expenditureThisYearList;
        for (int i = 1; i <= 12; i++) {
            double _expenditureSum = 0;
            String _selectionExpenditure = Expenditure.YEAR + "=" + TimeUtils.getYear() + " and " + Expenditure.MONTH + "=" + i + selection;
            _expenditureThisYearList = mJournalDataHelper.getExpenditureListFromDB(_selectionExpenditure);
            for (Expenditure _expenditure : _expenditureThisYearList) {
                _expenditureSum += _expenditure.getAmount();
            }
            _d[i - 1] = _expenditureSum;
        }
        return _d;
    }

    /*
     * 从数据库中获得当年每月的收入信息
     * */
    public double[] getIncomeThisYear(String selection) {
        double _d[] = new double[12];
        ArrayList<Income> _incomeThisYearList;
        for (int i = 1; i <= 12; i++) {
            double _incomeSum = 0;
            String _selectionIncome = Income.YEAR + "=" + TimeUtils.getYear() + " and " + Income.MONTH + "=" + i + selection;
            _incomeThisYearList = mJournalDataHelper.getIncomeListFromDB(_selectionIncome);
            for (Income _income : _incomeThisYearList) {
                _incomeSum += _income.getAmount();
            }
            _d[i - 1] = _incomeSum;
        }
        return _d;
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        super.onResume();
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
}
