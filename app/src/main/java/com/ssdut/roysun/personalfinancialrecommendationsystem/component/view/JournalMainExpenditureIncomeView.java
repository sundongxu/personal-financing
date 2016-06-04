package com.ssdut.roysun.personalfinancialrecommendationsystem.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by roysun on 16/3/12.
 * 自定义视图，支出、收入
 */
public class JournalMainExpenditureIncomeView extends View {

    private Paint mPaints;
    private float mStart;
    private float mSweep = 400;
    private int mColor, mLeft;
    private Bitmap mSrcBitmap;
    private String mLabel;
    private float mHeight;
    private float mRatio;

    public JournalMainExpenditureIncomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JournalMainExpenditureIncomeView(Context context, int color, int left, float ratio, String label) {
        super(context);
        mColor = color;
        mLeft = left;
        mLabel = label;
        mPaints = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaints = new Paint(mPaints);
        mRatio = ratio;
        mHeight = 100;
        mSrcBitmap = makeSrc(left);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArcs(canvas, mPaints, mLabel);
        mStart = 0;
        mSweep -= 5;
        if (mSweep < mHeight) {
            mSweep = mHeight;
        }
        invalidate();
    }

    Bitmap makeSrc(int left) {
        int iHeight = (int) (400 * mRatio);
        if (iHeight < 1) {
            iHeight = 1;
        }
        Bitmap bitmap = Bitmap.createBitmap(left + 100, iHeight, Bitmap.Config.ARGB_8888);  // 参数为宽width、高height
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        canvas.drawRect(left, iHeight, left + 100, 0, paint);  // 矩形绘制
        return bitmap;
    }

    private void drawArcs(Canvas canvas, Paint paint, String label) {
        canvas.drawBitmap(mSrcBitmap, mStart, mSweep, paint);
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setTextAlign(Paint.Align.CENTER);
        paint1.setTextSize(30);
        int iHeight = (int) (400 * mRatio);
        if (iHeight < 1) {
            iHeight = 1;
        }
//        canvas.drawText(label, 50 + mLeft, mHeight - paint1.getTextSize() - 10, paint1);
        canvas.drawText(label, 50 + mLeft, 480 - iHeight, paint1);
    }
}
