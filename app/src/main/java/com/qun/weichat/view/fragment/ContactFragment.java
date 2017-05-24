package com.qun.weichat.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qun.weichat.R;
import com.qun.weichat.adapter.ContactAdapter;
import com.qun.weichat.presenter.ContactPresenter;
import com.qun.weichat.presenter.ContactPresenterImpl;
import com.qun.weichat.utils.ToastUtil;
import com.qun.weichat.widget.ContactLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements ContactView, SwipeRefreshLayout.OnRefreshListener {

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
    }

    @Override
    public void onInit(List<String> contactsList) {
        mContactAdapter = new ContactAdapter(contactsList);
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
    public void onRefresh() {
        //更新通讯录
        mContactPresenter.onUpdate();
    }
}
