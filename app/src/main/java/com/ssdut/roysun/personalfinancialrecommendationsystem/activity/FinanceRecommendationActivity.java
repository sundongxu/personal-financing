package com.ssdut.roysun.personalfinancialrecommendationsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.engine.RecommendationEngine;

/**
 * Created by roysun on 16/5/18.
 * 理财推荐页面
 * 4个子功能卡片：①自选股列表 ②股票推荐 ③理财产品列表 ④理财产品推荐
 * ① 查看自选股，取消关注
 * ②
 */
public class FinanceRecommendationActivity extends BaseActivity {

    public static final String TAG = "FinanceRecommendationActivity";

    private LinearLayout mStockListArea;
    private LinearLayout mStockRecommendationArea;
    private LinearLayout mProductListArea;
    private TextView mCurUserNameText;
    private TextView mStockWatchedNumText;
    private TextView mStockIndexText;
    private TextView mProductExistNumText;
    private TextView mProductIndexText;
    private Spinner mDaysOptions;
    private int mDayNum;

    private StockManager mStockManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_recommendation);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mStockManager = StockManager.getInstance(this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (mToolbar != null) {
            mToolbar.setTitle("理财推荐");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mStockListArea = (LinearLayout) findViewById(R.id.ll_stock_list);
        mStockListArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转进自选股列表页面
                Intent _intent = new Intent(mContext, StockProductListActivity.class);
                // type = 0，表示为股票列表；type = 1，表示为理财产品列表
                _intent.putExtra("LIST_TYPE", 0);
                startActivity(_intent);
            }
        });
        mStockRecommendationArea = (LinearLayout) findViewById(R.id.ll_stock_recommendation);
        mStockRecommendationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转进推荐结果页面，要不要来个Material风的progressbar? -> 有啦
                Intent _intent = new Intent(mContext, RecommendationResultActivity.class);
                _intent.putExtra("PARAM_DAYS", mDayNum);
                startActivity(_intent);
            }
        });
        mProductListArea = (LinearLayout) findViewById(R.id.ll_product_list);
        mProductListArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转进理财产品列表页面
                Intent _intent = new Intent(mContext, StockProductListActivity.class);
                // type = 0，表示为股票列表；type = 1，表示为理财产品列表
                _intent.putExtra("LIST_TYPE", 1);
                startActivity(_intent);
            }
        });

        mCurUserNameText = (TextView) findViewById(R.id.tv_user_now);
        mCurUserNameText.setText("当前用户：" + mUserManager.getCurUser().getName());
        mStockWatchedNumText = (TextView) findViewById(R.id.tv_stock_num);
        int _iWatchedNum = mStockManager.getStockListFromDB("WATCHER_NAME='" + mUserManager.getCurUser().getName() + "'").size();
        mStockWatchedNumText.setText("关注股数：" + String.valueOf(_iWatchedNum));
        mStockIndexText = (TextView) findViewById(R.id.tv_stock_index_refer_to);
        mStockIndexText.setText("参考指标：乖离率");
        mProductExistNumText = (TextView) findViewById(R.id.tv_product_num);
        mProductExistNumText.setText("产品数目：6");
        mProductIndexText = (TextView) findViewById(R.id.tv_product_index_refer_to);
        mProductIndexText.setText("参考指标：余弦相似度");

        mDaysOptions = (Spinner) findViewById(R.id.sp_param_days);
        final SpinnerAdapter _adapter = new SpinnerAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.param_days));
        mDaysOptions.setAdapter(_adapter);
        mDaysOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mDayNum = RecommendationEngine.TERM_SHORT;
                        break;
                    case 1:
                        mDayNum = RecommendationEngine.TERM_MIDDLE;
                        break;
                    case 2:
                        mDayNum = RecommendationEngine.TERM_LONG;
                        break;
//                    case 3:
//                        mDayNum = 72;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Spinner适配器
    private class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTextSize(15);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            // android.R.id.text1 is default text view in resource of the android.
            // android.R.layout.simple_spinner_item is default layout in resources of android.

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTextSize(15);
            return convertView;
        }
    }

}
