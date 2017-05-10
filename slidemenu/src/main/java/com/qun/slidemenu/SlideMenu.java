package com.qun.slidemenu;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Qun on 2017/5/10.
 */

public class SlideMenu extends FrameLayout {

    public SlideMenu(@NonNull Context context) {
        this(context, null);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }
}
