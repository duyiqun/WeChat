package com.qun.weichat.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qun.weichat.R;
import com.qun.weichat.adapter.ContactAdapter;
import com.qun.weichat.event.ContactUpdateEvent;
import com.qun.weichat.presenter.ContactPresenter;
import com.qun.weichat.presenter.ContactPresenterImpl;
import com.qun.weichat.utils.ToastUtil;
import com.qun.weichat.view.activity.ChatActivity;
import com.qun.weichat.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactView, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.OnContactItemClickListener {

    private ContactPresenter mContactPresenter;
    private ContactLayout mContactLayout;
    private ContactAdapter mContactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactLayout = (ContactLayout) view;
        mContactPresenter = new ContactPresenterImpl(this);
        mContactPresenter.initContacts();
        mContactLayout.setOnRefreshListener(this);
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    /**
     * 1. 形参必须跟发送者发送的对象是一样的类型
     * 2. 添加注解
     * 3. 线程模型
     * 1）ThreadMode.MAIN 不管发送者在哪个线程发送的，该方法都在主线程中被调用
     * 2）ThreadMode.POSTING 发送者在哪个线程发送的,接收者就在哪个线程被调用
     * 3）ThreadMode.BACKGROUND 如果发送者是在子线程中被调用的，那么接收者也在这个线程中被调用
     * 如果发送者是在主线程中被调用的，那么接收者就会被EventBus内部的单线程池中被调用
     * 4）ThreadMode.ASYNC 不管发送者是在哪个线程发送的，接收者都会在新的子线程中被调用
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ContactUpdateEvent contactUpdateEvent) {
        ToastUtil.showMsg(getContext(), (contactUpdateEvent.isAdd ? "添加了：" : "删除了") + contactUpdateEvent.username);
        //更新通讯录，从环信把最新的通讯录更新下来
        mContactPresenter.onUpdate();
    }

    @Override
    public void onInit(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);
        mContactAdapter.setOnContactItemClickListener(this);
        mContactLayout.setAdapter(mContactAdapter);
    }

    @Override
    public void onUpdate(boolean isSuccess, String msg) {
        if (isSuccess) {
            mContactAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showMsg(getContext(), msg);
        }
        mContactLayout.setRefreshing(false);
        ToastUtil.showMsg(getContext(), "通讯录更新完毕");
    }

    @Override
    public void onDelete(boolean isSuccess, String msg, String username) {
        ToastUtil.showMsg(getContext(), msg);
    }

    @Override
    public void onRefresh() {
        //更新通讯录
        mContactPresenter.onUpdate();
    }

    @Override
    public void onClick(String username, int position) {
//        Intent intent = new Intent(getContext(), ChatActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }

    @Override
    public void onLongClick(final String username, int position) {
        //显示SnackBar，提示用户是否删除username
        Snackbar.make(mContactLayout, "你和" + username + "撕破脸了吗？", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactPresenter.delete(username);
            }
        }).show();
    }
}
