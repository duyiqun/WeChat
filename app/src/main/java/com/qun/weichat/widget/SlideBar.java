package com.qun.weichat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.qun.weichat.R;
import com.qun.weichat.adapter.IContactAdapter;
import com.qun.weichat.utils.StringUtils;

import java.util.List;

/**
 * Created by Qun on 2017/5/22.
 */

public class SlideBar extends View {

    private static final String[] SECTIONS = {"搜", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int mX;
    private int mAvgY;
    private Paint mPaint;
    private TextView mTvFloat;
    private RecyclerView mRecyclerView;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#9C9C9C"));
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //设置背景
                setBackgroundResource(R.drawable.slide_bar_bg);
                //显示FloatView
                //定位RecyclerView
                showFloatViewAndScrollRecyclerView(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //去除背景
                setBackgroundColor(Color.TRANSPARENT);
                //隐藏FloatView
                if (mTvFloat != null) {
                    mTvFloat.setVisibility(INVISIBLE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void showFloatViewAndScrollRecyclerView(float y) {
        int index = (int) (y / mAvgY);
        if (index < 0) {
            index = 0;
        } else if (index > SECTIONS.length - 1) {
            index = SECTIONS.length - 1;
        }
        String section = SECTIONS[index];
        if (mTvFloat == null) {
            ViewGroup parent = (ViewGroup) getParent();
            mTvFloat = (TextView) parent.findViewById(R.id.tv_float);
            mRecyclerView = (RecyclerView) parent.findViewById(R.id.recyclerView);
        }

        mTvFloat.setVisibility(VISIBLE);
        mTvFloat.setText(section);

        IContactAdapter adapter = (IContactAdapter) mRecyclerView.getAdapter();
        List<String> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (StringUtils.getInitial(data.get(i)).equalsIgnoreCase(section)) {
                mRecyclerView.smoothScrollToPosition(i);
                return;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        mX = measuredWidth / 2;
        mAvgY = measuredHeight / SECTIONS.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], mX, mAvgY * (i + 1), mPaint);
        }
    }
}
