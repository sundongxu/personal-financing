package com.ssdut.roysun.personalfinancialrecommendationsystem.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by roysun on 16/3/12.
 * 自定义视图
 */
public class JZPaintViewZandS extends View {
    private Paint mPaints;
    private float mStart;
    private float mSweep = 200;
    private int color, left;
    private Bitmap mSrcB;
    private String lable;
    //预算  余额   比率
    private float height;

    Bitmap makeSrc(int left) {
        Bitmap bm = Bitmap.createBitmap(40 + left, 250, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        c.drawRect(left, 0, left + 40, 250, p);
        return bm;
    }

    public JZPaintViewZandS(Context context, int color, int left, float height, String lable) {
        super(context);
        if (height >= 200) {
            this.height = 250 - 200;
        } else {
            this.height = 250 - height;
        }
        this.color = color;
        this.left = left;
        this.lable = lable;
        mPaints = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaints = new Paint(mPaints);
        mSrcB = makeSrc(left);
    }

    private void drawArcs(Canvas canvas, Paint paint, String lable) {
        canvas.drawBitmap(mSrcB, mStart, mSweep, paint);
        Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelP.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(lable, 20 + left, height - labelP.getTextSize() - 10, labelP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);//绘图背景色
        drawArcs(canvas, mPaints, lable);
        mStart = 0;
        mSweep -= 3;
        if (mSweep < height) {
            mSweep = height;
        }
        invalidate();
    }
}
