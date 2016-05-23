package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.StockDetailActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnItemTouchHelperMovedListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnItemTouchHelperSelectedListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnStartDragListener;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/18.
 * 股票主界面自选股列表 -> 跟当前用户绑定
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockItemViewHolder> implements OnItemTouchHelperSelectedListener {

    public static final String TAG = "StockListAdapter";

    private ArrayList<Stock> mStockList = new ArrayList<>();

    private OnStartDragListener mStartDragListener;
    private OnStockRemovedListener mStockRemovedListener;
    private Context mContext;

    public StockListAdapter(Context context, OnStartDragListener startDragListener, ArrayList<Stock> stockList) {
        mContext = context;
        mStartDragListener = startDragListener;
        mStockList = stockList;
    }

    @Override
    public StockListAdapter.StockItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        return new StockItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StockListAdapter.StockItemViewHolder holder, final int position) {
        holder.mNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
        int _textColor = mContext.getResources().getColor(R.color.black);
        holder.mNameText.setText(mStockList.get(position).getName());
        holder.mNameText.setTextColor(_textColor);
        holder.mNowPriceText.setText(String.format("%.2f", mStockList.get(position).getNowPrice()));
        holder.mNowPriceText.setTextColor(_textColor);
        double _increaseAmount = mStockList.get(position).getIncreaseAmount();
        double _increasePercentage = mStockList.get(position).getIncreasePersentage();
        String _increaseAmountText = String.format("%.2f", _increaseAmount);
        String _increasePerText = String.format("%.2f", _increasePercentage) + "%";
        int _increaseTextColor = mContext.getResources().getColor(R.color.black);
        if (_increaseAmount > 0) {
            _increaseTextColor = mContext.getResources().getColor(R.color.red);
            _increasePerText = "+" + _increasePerText;
            _increaseAmountText = "+" + _increaseAmountText;
        } else if (_increaseAmount < 0) {
            _increaseTextColor = mContext.getResources().getColor(R.color.green);
            _increasePerText = "-" + _increasePerText;
            _increaseAmountText = "-" + _increaseAmountText;
        }
        holder.mIncreasePercentageText.setText(_increasePerText);
        holder.mIncreasePercentageText.setTextColor(_increaseTextColor);
        holder.mIncreaseAmountText.setText(_increaseAmountText);
        holder.mIncreaseAmountText.setTextColor(_increaseTextColor);
        holder.mItemArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(position);
            }
        });
    }

    public void updateStockList(ArrayList<Stock> newStockList) {
        // 更新股票List后再notifyDataSetChanged
        mStockList = newStockList;
    }

    @Override
    public int getItemCount() {
        if (mStockList == null) {
            return 0;
        }
        return mStockList.size();
    }

    // 点击item后的跳转回调
    public void onItemClicked(int position) {
        // 跳转到股票详情页面，需要被选中的股票信息，放在intent里
        String _code = mStockList.get(position).getCode();
        Intent _intent = new Intent(mContext, StockDetailActivity.class);
        _intent.putExtra("CODE_SELECTED", _code);
        mContext.startActivity(_intent);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Log.v("sdx", "position=" + position + ", size=" + mStockList.size());
        mStockRemovedListener.onStockRemoved(mStockList.get(position));
//        mStockList.remove(position);  // 数组越界crash
        Log.v("sdx", "删除后size=" + mStockList.size());
        notifyItemRemoved(position);
    }

    // 通知Activityitem被删除的回调接口
    public void setStockRemovedListener(OnStockRemovedListener stockRemovedListener) {
        mStockRemovedListener = stockRemovedListener;
    }

    public interface OnStockRemovedListener {
        void onStockRemoved(Stock stock);
    }

    public class StockItemViewHolder extends RecyclerView.ViewHolder implements OnItemTouchHelperMovedListener {

        public LinearLayout mItemArea;
        public TextView mNameText;
        public TextView mNowPriceText;
        public TextView mIncreasePercentageText;
        public TextView mIncreaseAmountText;

        public StockItemViewHolder(View v) {
            super(v);
            mItemArea = (LinearLayout) v.findViewById(R.id.ll_stock_item);
            mNameText = (TextView) v.findViewById(R.id.tv_stock_watched_name);
            mNowPriceText = (TextView) v.findViewById(R.id.tv_stock_now_price);
            mIncreasePercentageText = (TextView) v.findViewById(R.id.tv_stock_increase_percentage);
            mIncreaseAmountText = (TextView) v.findViewById(R.id.tv_stock_increase_amount);
        }

        // 长按才触发
        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
        }

        // 长按后松手触发
        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
