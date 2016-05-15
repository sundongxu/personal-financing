package com.ssdut.roysun.personalfinancialrecommendationsystem.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.activity.MainActivity;

/**
 * Created by roysun on 16/5/3.
 * 主界面侧边抽屉的菜单列表适配器
 * ViewHolder优化，点击事件不好处理
 */
public class DrawerMenuListAdapter extends RecyclerView.Adapter<DrawerMenuListAdapter.ViewHolder> {

    public static final String TAG = "DrawerListAdapter";

    public static final int TYPE_HEADER = 1;  // Drawer新增的HeaderView
    public static final int TYPE_NORMAL = 2;  // 前面两个可点击视图，显示ImageView + TextView + SwitchCompat三个控件
    public static final int TYPE_SPECIAL = 3;  // 后面三个可点击视图，显示ImageView + TextView两个控件

    public static final int ITEM_SIGN_OUT = 3;
    public static final int ITEM_DEVICE_INFO = 4;
    public static final int ITEM_APP_INFO = 5;
    public static final int ITEM_EXIT = 6;

    private View mDrawerHeader;

    private Context mContext;
    private String[] mMenuItemNameList;
    private OnItemClickListener mListener;
    private Resources mResources;

    public DrawerMenuListAdapter(Context context) {
        mContext = context;
        mMenuItemNameList = initItemNameList();
    }

    public String[] initItemNameList() {
        mResources = mContext.getResources();
        return new String[]{mResources.getString(R.string.drawer_item_0),
                mResources.getString(R.string.drawer_item_1),
                mResources.getString(R.string.drawer_item_2),
                mResources.getString(R.string.drawer_item_3),
                mResources.getString(R.string.drawer_item_4),
                mResources.getString(R.string.drawer_item_5)};
    }

    @Override
    public int getItemViewType(int position) {
        if (mDrawerHeader == null) {
            if (position >= 0 && position < 2) {
                return TYPE_NORMAL;
            } else {
                return TYPE_SPECIAL;
            }
        } else {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position > 0 && position < 3) {
                return TYPE_NORMAL;
            } else {
                return TYPE_SPECIAL;
            }
        }
    }

    // 对外暴露设置RecycleView的HeaderView的接口（类似ListView的addHeaderView()）
    public void setHeaderView(View headerView) {
        mDrawerHeader = headerView;
        notifyItemInserted(0);
    }

    @Override
    public DrawerMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(mDrawerHeader);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;  //提前结束该方法，Header不需要绑定信息，事件处理可以放到MainActivityMD中去
        }
        // 走下来position至少是1，所以getRealPosition方法get到的pos至少是0，数组不会越界，放心！
        final int pos = getRealPosition(holder);

        // 无论NORMAL还是SPECIAL都可这样设置TextView的文案
        holder.mItemText.setText(mMenuItemNameList[pos]);

        if (mContext instanceof MainActivity) {
            // 无header
            switch (pos) {
                case 0:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_0));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ((MainActivity) mContext).updateBottomNavigationColor(isChecked);
                        }
                    });
                    break;
                case 1:
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_1));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ((MainActivity) mContext).updateBottomNavigationItems(isChecked);
                        }
                    });
                    break;
                case 2:
                    // 点击的ripple效果
                    holder.mMenuItemArea.setBackgroundResource(R.drawable.item_click_ripple);
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_2_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.mMenuItemArea.setBackgroundResource(R.drawable.item_click_ripple);
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_3_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.mMenuItemArea.setBackgroundResource(R.drawable.item_click_ripple);
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_4_colored));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
                    break;
                case 5:
                    holder.mMenuItemArea.setBackgroundResource(R.drawable.item_click_ripple);
                    holder.mItemPic.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_drawer_item_5));
                    holder.mItemText.setVisibility(View.VISIBLE);
                    holder.mBtnChange.setVisibility(View.GONE);
            }
            holder.mMenuItemArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.OnItemClick(v, position);
                    }
                }
            });
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mDrawerHeader == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mDrawerHeader == null ? mMenuItemNameList.length : mMenuItemNameList.length + 1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // RecycleView的Item点击响应
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mMenuItemArea;
        public ImageView mItemPic;
        public TextView mItemText;
        public SwitchCompat mBtnChange;

        public ViewHolder(View v) {
            super(v);
            if (v == mDrawerHeader) {
                return;
            }
            mMenuItemArea = (RelativeLayout) v.findViewById(R.id.rl_menu_item);
            mItemPic = (ImageView) v.findViewById(R.id.iv_menu_item);
            mItemText = (TextView) v.findViewById(R.id.tv_menu_item);
            mBtnChange = (SwitchCompat) v.findViewById(R.id.sc_menu_item);
        }
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
//        if (mContext instanceof MainActivity) {
//            if (position == 0 || position == 1) {
//                // 加载调整底部tab的Style、Num的两个SwitchCompat按钮布局
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_menu_list_item_0_1_main, null);
//                SwitchCompat _btnChange = (SwitchCompat) convertView.findViewById(R.id.sc_menu_item);
//                String _scText = position == 0 ? mMenuItemNameList.get(0) : mMenuItemNameList.get(1);
//                _btnChange.setText(_scText);
//                _btnChange.setChecked(((MainActivity) mContext).isBottomNavigationColored());
//                _btnChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        switch (position) {
//                            case 0:
//                                ((MainActivity) mContext).updateBottomNavigationColor(isChecked);
//                                break;
//                            case 1:
//                                ((MainActivity) mContext).updateBottomNavigationItems(isChecked);
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
//                                ((MainActivity) mContext).exitApplication();
//                                break;
//                        }
//                    }
//                });
//
//            }
//        }
//        return convertView;
//    }


