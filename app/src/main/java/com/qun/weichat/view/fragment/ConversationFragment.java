package com.qun.weichat.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.qun.weichat.R;
import com.qun.weichat.adapter.ConversationAdapter;
import com.qun.weichat.presenter.ConversationPresenter;
import com.qun.weichat.presenter.ConversationPresenterImpl;
import com.qun.weichat.view.activity.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.OnConversationClickListener {

    private RecyclerView mRecyclerView;
    private ConversationPresenter mConversationPresenter;
    private ConversationAdapter mConversationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mConversationPresenter = new ConversationPresenterImpl(this);
        mConversationPresenter.initConversation();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onInitConversation(List<EMConversation> emConversationList) {
        mConversationAdapter = new ConversationAdapter(emConversationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mConversationAdapter);
        mConversationAdapter.setOnConversationClickListener(this);
    }

    @Override
    public void onUpdate() {
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConversationClick(EMConversation conversation, int position) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("username", conversation.getLastMessage().getUserName());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        mConversationPresenter.updateConversation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        mRecyclerView = null;
        mConversationAdapter = null;
    }
}
