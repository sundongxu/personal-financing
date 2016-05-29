package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.InvestmentSimulationActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.bean.Stock;

import java.util.ArrayList;

/**
 * Created by roysun on 16/5/28.
 * 资产列表适配器
 */
public class AssetInfoListAdapter extends RecyclerView.Adapter<AssetInfoListAdapter.AssetItemViewHolder> {

    public static final String TAG = "AssetInfoListAdapter";

    private Context mContext;
    private ArrayList<Stock> mStockList;
    private OnItemClickListener mListener;
    private double mTotalAssetAmount;

    public AssetInfoListAdapter(Context context, ArrayList<Stock> stockList) {
        mContext = context;
        mStockList = stockList;
    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

    @Override
    public AssetItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);
        AssetItemViewHolder viewHolder = new AssetItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AssetItemViewHolder holder, final int position) {
        if (mStockList.get(position).getIncreaseAmount() >= 0) {
            holder.mAssetIcon.setImageResource(R.drawable.icon_stock_up);
        } else {
            holder.mAssetIcon.setImageResource(R.drawable.icon_stock_down);
        }
        holder.mAssetTitleText.setText(mStockList.get(position).getName());
        holder.mAssetOwnNumText.setText("持有股数：" + String.valueOf(mStockList.get(position).getBuyNum()));
        holder.mAssetTotalValueText.setText("总价值：" + String.format("%.2f", mStockList.get(position).getNowPrice() * mStockList.get(position).getBuyNum()));
        holder.mItemArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(mStockList.get(position));
                }
            }
        });

        mTotalAssetAmount += Double.valueOf(String.format("%.2f", mStockList.get(position).getNowPrice() * mStockList.get(position).getBuyNum()));

        if (position == mStockList.size() - 1) {
            if (mContext instanceof InvestmentSimulationActivity) {
                ((InvestmentSimulationActivity) mContext).setTotalAsset(mTotalAssetAmount);
            }
            mTotalAssetAmount = 0;
        }
    }

    public void updateStockList(ArrayList<Stock> stockList) {
        mStockList = stockList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Stock stock);
    }

    public class AssetItemViewHolder extends RecyclerView.ViewHolder {
        // 用户信息条目内容
        public RelativeLayout mItemArea;
        public ImageView mAssetIcon;
        public TextView mAssetTitleText;
        public TextView mAssetOwnNumText;
        public TextView mAssetTotalValueText;

        public AssetItemViewHolder(View v) {
            super(v);
            mItemArea = (RelativeLayout) v.findViewById(R.id.rl_asset_item);
            mAssetIcon = (ImageView) v.findViewById(R.id.iv_asset_icon);
            mAssetTitleText = (TextView) v.findViewById(R.id.tv_asset_title);
            mAssetOwnNumText = (TextView) v.findViewById(R.id.tv_asset_own_number);
            mAssetTotalValueText = (TextView) v.findViewById(R.id.tv_asset_total_value);
        }
    }
}
