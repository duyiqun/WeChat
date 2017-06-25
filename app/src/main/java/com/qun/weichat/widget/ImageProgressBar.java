package com.qun.weichat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hyphenate.util.DensityUtil;
import com.qun.weichat.R;

/**
 * Created by Qun on 2017/6/25.
 */

public class ImageProgressBar extends View {

    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private float mProgress;
    private Paint mPaint;
    private Bitmap mBitmap;

    public ImageProgressBar(Context context) {
        this(context, null);
    }

    public ImageProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(context, 18));
        mPaint.setColor(Color.WHITE);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_error);
    }

    public ImageProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ImageProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        //该方法可以保证不管在哪个线程中调用，都能让重绘方法在主线程中执行
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mProgress == -1) {
            canvas.drawBitmap(mBitmap, mMeasuredWidth / 2 - mBitmap.getWidth() / 2, mMeasuredHeight / 2 - mBitmap.getHeight() / 2, null);
            return;
        }
        canvas.drawText(mProgress + "%", mMeasuredWidth / 2, mMeasuredHeight / 2, mPaint);
        //裁剪出一个合适的区域
        canvas.clipRect(0, mMeasuredHeight * mProgress / 100, mMeasuredWidth, mMeasuredHeight);
        canvas.drawColor(Color.parseColor("#559c9c9c"));
    }
}
