package com.qun.weichat.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qun.weichat.R;

import org.w3c.dom.Text;

/**
 * Created by Qun on 2017/5/22.
 */

public class ContactLayout extends RelativeLayout {

    private TextView mTvFloat;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SlideBar mSlideBar;

    public ContactLayout(Context context) {
        this(context, null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.contact_layout, this, true);
        mTvFloat = (TextView)findViewById(R.id.tv_float);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSlideBar = (SlideBar) findViewById(R.id.slideBar);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }
}
