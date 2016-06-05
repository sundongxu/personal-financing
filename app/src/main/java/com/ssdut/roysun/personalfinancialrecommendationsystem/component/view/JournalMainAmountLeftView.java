package com.ssdut.roysun.personalfinancialrecommendationsystem.component.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by roysun on 16/3/12.
 * 自定义视图，余额
 */
public class JournalMainAmountLeftView extends View {

    private Paint mPaints;
    private Paint mFramePaint;
    private boolean mUseCenters;
    private RectF mBigOval;
    private float mStart;
    private float mSweep;
    private int mStepping;
    private float mRatio;

    public JournalMainAmountLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JournalMainAmountLeftView(Context context, float budget, float amountLeft, int color, int stepping) {
        super(context);
        mRatio = amountLeft / (budget / 360);    //比率  (控制绘图区域)
        mStepping = stepping;
        mPaints = new Paint();
        mPaints = new Paint(mPaints);
        mPaints.setColor(color);
        mUseCenters = true;
        mBigOval = new RectF(15, 0, 175, 160);//绘图区域 距左， 距上 ，左起点至右距离 ，上起点距下距离
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(0);
    }

    private void drawArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) {
        canvas.drawRect(oval, mFramePaint);
        canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);//绘图背景色
        drawArcs(canvas, mBigOval, mUseCenters, mPaints);
        mStart = 0;
        mSweep += mStepping;
        if (mSweep > mRatio) {
            mSweep = mRatio;
        }
        invalidate();
    }
}
