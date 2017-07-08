package com.qun.weichat.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.qun.weichat.R;
import com.qun.weichat.adapter.SearchFriendAdapter;
import com.qun.weichat.presenter.AddFriendPresenter;
import com.qun.weichat.presenter.AddFriendPresenterImpl;
import com.qun.weichat.utils.ToastUtil;

import java.util.List;

public class AddFriendActivity extends BaseActivity implements SearchView.OnQueryTextListener, AddFriendView, SearchFriendAdapter.OnAddBtnClickListener {

    private TextView mTvTitle;
    private Toolbar mToolBar;
    private ImageView mIvNodata;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private AddFriendPresenter mAddFriendPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvNodata = (ImageView) findViewById(R.id.iv_nodata);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setQueryHint("用户名(支持模糊匹配)");
        //设置SearchView中文本改变监听器
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当点击软键盘的Action键的时候被回调
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
//        ToastUtil.showMsg(this, "开始搜索:" + query);
        showProgress("正在搜索");
        mAddFriendPresenter.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ToastUtil.showMsg(this, newText);
        return true;
    }

    @Override
    public void onSearch(boolean isSuccess, String msg, List<AVUser> userList, List<String> myFriendList) {
        hideProgress();
        //取消SearchView的焦点
        mSearchView.clearFocus();
        if (!isSuccess) {
            ToastUtil.showMsg(this, msg);
            //显示nodata，隐藏RecyclerView
            mIvNodata.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mIvNodata.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            SearchFriendAdapter searchFriendAdapter = new SearchFriendAdapter(userList, myFriendList);
            searchFriendAdapter.setOnAddBtnClickListener(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(searchFriendAdapter);
        }
    }

    @Override
    public void onClick(AVUser avUser, int position) {
        mAddFriendPresenter.addFriend(avUser.getUsername());
    }

    @Override
    public void onAddFiend(boolean isSuccess, String msg, String username) {
        if (!isSuccess) {
            ToastUtil.showMsg(this, msg);
        } else {
            Snackbar.make(mRecyclerView, "给" + username + "发送邀请成功", Snackbar.LENGTH_SHORT).show();
        }
    }
}
