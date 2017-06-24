package com.qun.weichat.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Qun on 2017/6/24.
 */

public class KeyboardListenerLinearLayout extends LinearLayout implements View.OnLayoutChangeListener {

    private int mHeight;

    public KeyboardListenerLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public KeyboardListenerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardListenerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public KeyboardListenerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            mHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        } else {
            mHeight = 2000;
        }
        this.addOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mOnKeyboardChangedListener != null) {
            if (oldBottom - bottom >= mHeight / 3) {
                //打开软件盘了
                mOnKeyboardChangedListener.onKeyboardChanged(true);
            } else if (oldBottom - bottom < mHeight / 3) {
                //关闭软件盘了
                mOnKeyboardChangedListener.onKeyboardChanged(false);
            }
        }
    }

    public interface OnKeyboardChangedListener {
        void onKeyboardChanged(boolean isOpen);
    }

    private OnKeyboardChangedListener mOnKeyboardChangedListener;

    public void setOnKeyboardChangedListener(OnKeyboardChangedListener onKeyboardChangedListener) {
        this.mOnKeyboardChangedListener = onKeyboardChangedListener;
    }
}
