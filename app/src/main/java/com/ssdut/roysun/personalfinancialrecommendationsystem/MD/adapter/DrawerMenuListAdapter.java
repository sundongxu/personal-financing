package com.ssdut.roysun.personalfinancialrecommendationsystem.MD.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.MD.activity.MainActivityMD;
import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.AppInfoActivity;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.DeviceInfoActivity;

/**
 * Created by roysun on 16/5/3.
 * 主界面侧边抽屉的菜单列表适配器
 * ViewHolder优化，点击事件不好处理
 */
public class DrawerMenuListAdapter extends RecyclerView.Adapter<DrawerMenuListAdapter.ViewHolder> {

    public static final String TAG = "DrawerMenuListAdapter";

    private String[] mMenuItemNameList;
    private Context mContext;

    public void setListener(OnDrawerItemSelectedListener listener) {
        mListener = listener;
    }

    private OnDrawerItemSelectedListener mListener;

    public DrawerMenuListAdapter(Context context, String[] itemList) {
        mContext = context;
        mMenuItemNameList = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mItemPic;
        public TextView mItemText;
        public SwitchCompat mBtnChange;

        public ViewHolder(View v) {
            super(v);
            mItemPic = (ImageView) v.findViewById(R.id.iv_menu_item);
            mItemText = (TextView) v.findViewById(R.id.tv_menu_item);
            mBtnChange = (SwitchCompat) v.findViewById(R.id.sc_menu_item);
            v.setOnClickListener(this);
            System.out.println("sdx---位置:" + getPosition());
        }

        @Override
        public void onClick(View view) {
            if (mContext instanceof MainActivityMD) {
                switch (mItemText.getText().toString()) {
                    case "设备信息":
                        mContext.startActivity(new Intent(mContext, DeviceInfoActivity.class));
                        mListener.onItemSelectedFinished();
                        break;
                    case "关于":
                        mContext.startActivity(new Intent(mContext, AppInfoActivity.class));
                        mListener.onItemSelectedFinished();
                        break;
                    case "退出":
                        ((MainActivityMD) mContext).exitApplication();
                        mListener.onItemSelectedFinished();
                        break;

                }
            }
        }
    }

    @Override
    public DrawerMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mItemText.setText(mMenuItemNameList[position]);
        if (mContext instanceof MainActivityMD) {
            switch (position) {
                case 0:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_1_main_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ((MainActivityMD) mContext).updateBottomNavigationColor(isChecked);
                        }
                    });
                    break;
                case 1:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_2_main_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ((MainActivityMD) mContext).updateBottomNavigationItems(isChecked);
                        }
                    });
                    break;
                case 2:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_4_main_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_3_main_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_5_main_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mMenuItemNameList.length;
    }


    // 通知Activity已经点击过Menu item了，此时应该关闭抽屉
    public interface OnDrawerItemSelectedListener {
        public void onItemSelectedFinished();
    }
}


//ListView + BaseAdapter版本
//    private Context mContext;
//    private ArrayList<String> mMenuItemList;  // 条目文案
//
//    public DrawerMenuListAdapter(Context context, ArrayList<String> menuItemNameList) {
//        mContext = context;
//        mMenuItemNameList = menuItemNameList;
//    }
//
//    @Override
//    public int getCount() {
//        return mMenuItemNameList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mMenuItemNameList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (mContext instanceof MainActivityMD) {
//            if (position == 0 || position == 1) {
//                // 加载调整底部tab的Style、Num的两个SwitchCompat按钮布局
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_menu_list_item_0_1_main, null);
//                SwitchCompat _btnChange = (SwitchCompat) convertView.findViewById(R.id.sc_menu_item);
//                String _scText = position == 0 ? mMenuItemNameList.get(0) : mMenuItemNameList.get(1);
//                _btnChange.setText(_scText);
//                _btnChange.setChecked(((MainActivityMD) mContext).isBottomNavigationColored());
//                _btnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        switch (position) {
//                            case 0:
//                                ((MainActivityMD) mContext).updateBottomNavigationColor(isChecked);
//                                break;
//                            case 1:
//                                ((MainActivityMD) mContext).updateBottomNavigationItems(isChecked);
//                                break;
//                        }
//                    }
//                });
//
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 消化没点到SwitchCompat的操作
//                    }
//                });
//
//            } else {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_menu_list_item_2_3_4_main, null);
//                TextView _menuItemView = (TextView) convertView.findViewById(R.id.tv_drawer_item);
//                String _tvText = "";
//                switch (position) {
//                    case 2:
//                        _tvText = mMenuItemNameList.get(2);
//                        break;
//                    case 3:
//                        _tvText = mMenuItemNameList.get(3);
//                        break;
//                    case 4:
//                        _tvText = mMenuItemNameList.get(4);
//                        break;
//                }
//                _menuItemView.setText(_tvText);
//
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        switch (position) {
//                            case 2:
//                                mContext.startActivity(new Intent(mContext, DeviceInfoActivity.class));
//                                break;
//                            case 3:
//                                mContext.startActivity(new Intent(mContext, AppInfoActivity.class));
//                                break;
//                            case 4:
//                                ((MainActivityMD) mContext).exitApplication();
//                                break;
//                        }
//                    }
//                });
//
//            }
//        }
//        return convertView;
//    }


