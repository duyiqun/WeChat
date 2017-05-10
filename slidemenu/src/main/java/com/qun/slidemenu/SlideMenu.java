package com.qun.slidemenu;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Qun on 2017/5/10.
 */

public class SlideMenu extends FrameLayout {

    private static final String TAG = "SlideMenu";
    private ViewDragHelper mViewDragHelper;

    public SlideMenu(@NonNull Context context) {
        this(context, null);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             * 用于判断当前选中的childView是否能够被拖拽
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.d(TAG, "tryCaptureView: child=" + child + "/pointerId=" + pointerId);
                return true;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }

            /**
             *
             * @param child
             * @param left 预计拖动的距离
             * @param dx 不断的滑动过程中的每个小的距离
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.d(TAG, "clampViewPositionHorizontal: child=" + child + "/left=" + left + "/dx=" + dx);
                return left;
            }

            /**
             * 如果这个SlideMenu不支持垂直方向的拖拽，那么不管getViewVerticalDragRange返回什么值都不拦截子控件
             * 如果这个SlideMenu支持垂直方向的拖拽，那么在垂直方法是否拦截就得看getViewVerticalDragRange的返回值了，返回值大于0则拦截
             * @param child
             * @param top
             * @param dy
             * @return
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //还是将触摸事件交给工具类处理
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
