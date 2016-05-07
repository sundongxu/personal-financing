package com.ssdut.roysun.personalfinancialrecommendationsystem.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssdut.roysun.personalfinancialrecommendationsystem.R;
import com.ssdut.roysun.personalfinancialrecommendationsystem.listener.OnTabClickListener;
import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.ViewUtils;

/**
 * Created by roysun on 16/4/9.
 * 自定义顶部tab导航栏，配合Fragment中的ViewPager+子Fragment使用
 */
public class TopTabBarView extends LinearLayout {

    public static final String TAG = "TopTabBarView";

    public SparseArray<TextView> mTabViews;
    public String[] mTabTitles;

    public int mCurrentTabIndex = 0;//当前选中的tab的索引
    public float indexPositionOffset = 0;//当前指示游标

    //上次选中的tabId
    public int lastTabId = -1;
    public static final int START_ID_INDEX = 0x00000088;//view id的起始值
    public OnTabClickListener mTabClick = null;

    public Paint mIndexPaint;

    public static final float MAX_TITLE_COUNT_ON_SIGHT = 4f;// 一次可视title最大个数

    public int mWidth;
    public float mMaxTitleCount;
    public float mScrollbarWidth = 0.0f;
    public int mTabMaxWidth;

    public Context mContext;

    public HorizontalScrollView mScrollView;

    public LinearLayout mTitleContainer;


    public TopTabBarView(Context context) {
        this(context, null);
    }

    public TopTabBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopTabBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        setGravity(Gravity.LEFT);
        mIndexPaint = new Paint();
        mIndexPaint.setAntiAlias(true);
        mIndexPaint.setStyle(Paint.Style.FILL);
        mIndexPaint.setColor(getResources().getColor(R.color.top_tabbar_text_color));
        mIndexPaint.setStrokeWidth(getTabHeight());

        setWillNotDraw(false);
        setOrientation(HORIZONTAL);

        mScrollView = new HorizontalScrollView(mContext);
        mScrollView.setFadingEdgeLength(0);
        mScrollView.setHorizontalScrollBarEnabled(false);

        mTitleContainer = new LinearLayout(mContext) {
            @Override
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                //绘制底线
                //canvas.drawLine(0, getHeight() - getShadowHeight(), getWidth(), getHeight() - getShadowHeight(), mShadowPaint);

                //绘制游标线
                onDrawScrollbarLine(canvas, getHeight());
            }
        };
        mTitleContainer.setWillNotDraw(false);
        mTitleContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTitleContainer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mScrollView.addView(mTitleContainer);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, getSpecialHeight());
        addView(mScrollView, lp);
    }

    //绘画游标线
    public void onDrawScrollbarLine(Canvas canvas, int height) {
        if (canvas != null) {
            //绘画游标线
            int mIndexMarginLeft = (int) ((mCurrentTabIndex + indexPositionOffset) * getTabWidth());
            int widthOffset = (getTabWidth() - (int) mScrollbarWidth - ViewUtils.dip2px(getContext(), 5.0f)) / 2;
            float scrollbarEndPos = mIndexMarginLeft + getTabWidth();

            if (widthOffset > 0) {
                if (mScrollbarWidth > 0) {
                    scrollbarEndPos = mIndexMarginLeft + widthOffset + mScrollbarWidth + ViewUtils.dip2px(getContext(), 5.0f);
                }
            } else {
                //直接使用默认的，，TextView的大小，，不是使用文字的大小
                widthOffset = 0;
            }

            canvas.drawLine(mIndexMarginLeft + widthOffset, height - getTabHeight() / 2/* - getShadowHeight()*/, scrollbarEndPos, height - getTabHeight() / 2/* - getShadowHeight()*/,
                    mIndexPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        reLayout();

    }

    /**
     * 获取边界宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // Default size if no limits are specified.
        int result = 480;// px
        if (specMode == MeasureSpec.AT_MOST) {
            // Calculate the ideal size of your control
            // within this maximum size.
            // If your control fills the available space
            // return the outer bound.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // If your control can fit within these bounds return that value.
            result = specSize;
        }

        return result;
    }

    /**
     * 重新计算宽度
     */
    private void reLayout() {
        mTabMaxWidth = (int) (mWidth / mMaxTitleCount);
        int childCount = mTitleContainer.getChildCount();
        ViewGroup.LayoutParams titleContainerLP = mTitleContainer.getLayoutParams();
        titleContainerLP.width = mWidth * childCount;
        mTitleContainer.setLayoutParams(titleContainerLP);
        for (int i = 0; i < childCount; i++) {
            View child = mTitleContainer.getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            lp.width = mTabMaxWidth;
            child.setLayoutParams(lp);
        }
        invalidate();
    }

    public void setTabs(String[] tabTitles) {
        if (tabTitles == null || tabTitles.length == 0) {
            mTabViews = new SparseArray<TextView>(1);
        } else {
            mTabViews = new SparseArray<TextView>(tabTitles.length);
        }

        int length = tabTitles != null ? tabTitles.length : 0;

        mMaxTitleCount = length < MAX_TITLE_COUNT_ON_SIGHT ? length : MAX_TITLE_COUNT_ON_SIGHT;

        this.mTabTitles = tabTitles;
        if (mTabTitles != null && mTabTitles.length > 0) {
            for (int i = 0; i < mTabTitles.length; i++) {
                TextView tabText = new TextView(getContext());
                LinearLayout.LayoutParams tabTextPrams = new LinearLayout.LayoutParams(mTabMaxWidth, getSpecialHeight());
                tabText.setText(mTabTitles[i]);
                tabText.setGravity(Gravity.CENTER);
                tabText.setSingleLine(true);
                tabText.setEllipsize(TextUtils.TruncateAt.END);
                tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTableTextSize());
                tabText.setTextColor(getContext().getResources().getColor(R.color.top_tabbar_unselected_color));
                tabText.setBackgroundResource(R.drawable.top_tabbar_text_background);
                mTitleContainer.addView(tabText, tabTextPrams);
                mTabViews.append(i, tabText);

                mScrollbarWidth = (getTextWidth(tabText) > mScrollbarWidth) ? getTextWidth(tabText) : mScrollbarWidth;
            }
        }
    }

    private float getTextWidth(TextView tabText) {
        if (tabText != null && tabText.getText() != null) {
            TextPaint paint = tabText.getPaint();
            return Layout.getDesiredWidth(tabText.getText().toString(), 0, tabText.getText().length(), paint);
        }

        return 0.0f;
    }

    private void updateTabStatus(TextView currentTab) {
        int[] location = new int[2];
        currentTab.getLocationOnScreen(location);
        int currentIndex = mTabViews.indexOfValue(currentTab);
        int size = mTabViews.size();
        int selectX = location[0];
        int currentWidth = selectX;
        // if (selectX < mChildViewMaxWidth && currentIndex > 0) {
        // if (selectX < currentWidth && currentIndex > 0) {
        // // 当点击位置 小于 一个可视按钮 且 不是第一个btn
        // mScrollView.smoothScrollBy(-(currentWidth - selectX), location[1]);
        // } else
        if ((mWidth - selectX) < (2 * currentWidth)) {
            // 当点击位置 距离屏幕右侧 小于 两个可视按钮
            mScrollView.smoothScrollBy(currentWidth, location[1]);
        } else if (currentIndex == 0 || currentIndex == 1) {
            mScrollView.smoothScrollTo(0, location[1]);
        } else if (currentIndex == size - 1) {
            mScrollView.smoothScrollTo(mWidth, location[1]);
        }

    }

    //选中哪个tab
    public void onTabSelected(int index, boolean hasAnim) {
        if (lastTabId == index) {
            return;
        }
        lastTabId = index;
        TextView tabText;
        int size = mTabViews.size();
        for (int i = 0; i < size; i++) {
            tabText = mTabViews.get(i);
            if (tabText != null) {
                if (i == index) {
                    tabText.setTextColor(getContext().getResources().getColor(R.color.top_tabbar_selected_color));
                    updateTabStatus(tabText);
                } else {
                    tabText.setTextColor(getContext().getResources().getColor(R.color.top_tabbar_unselected_color));
                }
            }
        }
    }

    public float getTableTextSize() {
        return 15.0f;
    }

    public int getSpecialHeight() {
        return ViewUtils.dip2px(getContext(), 41.0f);
    }

    /*
     * 初始化选中某个tab
	 * */
    public void init(int tabIndex) {
        mCurrentTabIndex = tabIndex;
        onTabSelected(tabIndex, false);
        DefaultTabClickListener defaultTabClickListener = new DefaultTabClickListener();
        for (int i = 0; i < mTabTitles.length; i++) {
            View view = getTabView(i);
            if (view != null) {
                view.setId(START_ID_INDEX + i);
                view.setOnClickListener(defaultTabClickListener);
            }
        }
        mTitleContainer.invalidate();
    }

    /*
     * 设置监听
     * */
    public void setTabClickListener(OnTabClickListener listener) {
        mTabClick = listener;
    }

    private class DefaultTabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            for (int i = 0; i < mTabTitles.length; i++) {
                if (getTabView(i) != null && id == getTabView(i).getId()) {
                    onTabSelected(i, true);
                    if (mTabClick != null) {
                        mTabClick.onTabClick(v, OnTabClickListener.TYPE_REAL);
                    }
                }
            }
        }
    }

    /**
     * @param index 从0开始取值，0为第一个tab
     * @return
     */
    public View getTabView(int index) {
        if (index < 0) {
            return null;
        }
        if (mTabViews.size() >= index + 1) {
            return mTabViews.get(index);
        } else {
            return null;
        }
    }

    /**
     * @return get single tab width
     */
    public int getTabWidth() {
        if (mTabTitles == null || mTabTitles.length <= 0) {
            return mWidth;
        }
        return mTabMaxWidth;
    }

    public int getTabHeight() {
        return ViewUtils.dip2px(getContext(), 2);
    }

    public void setTextSize(float size) {
        TextView tabText;
        int length = mTabViews.size();
        for (int i = 0; i < length; i++) {
            tabText = mTabViews.get(i);
            if (tabText != null) {
                tabText.setTextSize(size);
            }
        }
    }

    /**
     * @param tabIndex current tab index
     * @param offset   offset in percent
     */
    public void onTabScrolled(int tabIndex, float offset) {
//		XLog.d(TAG, "tabIndex:" + tabIndex + " offset:" + offset);
        mCurrentTabIndex = tabIndex;
        indexPositionOffset = offset;
        mTitleContainer.invalidate();
    }
}
