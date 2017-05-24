package com.qun.weichat.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.weichat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.iv_nodata)
    ImageView mIvNodata;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);

    }
}
