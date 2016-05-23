package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.db.manager.StockManager;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.SnackbarClickListener;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by roysun on 16/5/21.
 * 理财建议页面卡片List数据适配器
 * 目前两种情况都想复用这个adapter，加以区分: 其实不需要区分 -> item视图后面加个购买按钮就可以了
 * ① 自选股列表
 * ② 推荐股列表
 */
public class StockProductListAdapter extends BaseAdapter {

    public static final String TAG = "StockProductListAdapter";
    private ArrayList<Stock> mStockList;
    private LayoutInflater mLayoutInflater;
    private ImageView mIcon;
    private TextView mCodeText;
    private TextView mNameText;
    private StockManager mStockManager;
    private Context mContext;

    public StockProductListAdapter(Context context, ArrayList<Stock> stockList, StockManager stockManager) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mStockList = stockList;
        mStockManager = stockManager;
    }

    public void initStockList() {
        if (mStockList == null) {
            mStockList = new ArrayList<>();
        } else {
            mStockList.clear();
        }
        for (int i = 0; i < 10; i++) {
            Stock _stock = new Stock();
            _stock.setName("华天科技");
            _stock.setCode("sz002185");
            mStockList.add(_stock);
        }
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convertView对象就是item的界面对象，只有为空的时候我们才需要重新赋值一次，这样可以提高效率，如果有这个对象的话，系统会自动复用
        //item_listview就是自定义的item的布局文件
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_watched_item, null);
        }
        //注意findViewById的时候，要使用convertView的这个方法，因为是在它里面进行控件的寻找
        mIcon = (ImageView) convertView.findViewById(R.id.iv_stock_watched_icon);
        mCodeText = (TextView) convertView.findViewById(R.id.tv_stock_watched_code);
        mNameText = (TextView) convertView.findViewById(R.id.tv_stock_watched_name);
        //将数据与控件进行绑定
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog _materialDialog = new MaterialDialog(mContext);
                _materialDialog.setMessage("确定将" + mStockList.get(position).getName() + "从自选股列表中删除？");
                _materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _materialDialog.dismiss();
                    }
                });
                _materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 从当前List中删除
                        if (mStockManager.deleteStock(mStockList.get(position).getId()) == 1) {
                            Snackbar.make(mIcon, "自选股删除成功！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        } else {
                            Snackbar.make(mIcon, "自选股删除失败！", Snackbar.LENGTH_LONG).setAction(R.string.snackbar_hint, new SnackbarClickListener()).show();
                        }
                        onItemRemoved(position);
                        _materialDialog.dismiss();
                    }
                });
                _materialDialog.setCanceledOnTouchOutside(true).show();
            }
        });
        mCodeText.setText(mStockList.get(position).getCode());
        mNameText.setText(mStockList.get(position).getName());
        return convertView;
    }

    public void onItemRemoved(int position) {
        // 执行在Snackbar显示之前
        mStockList.remove(position);
        notifyDataSetChanged();
    }

}
