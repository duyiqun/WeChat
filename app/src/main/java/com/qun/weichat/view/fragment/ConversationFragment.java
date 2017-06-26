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
import com.qun.weichat.R;
import com.qun.weichat.adapter.ConversationAdapter;
import com.qun.weichat.presenter.ConversationPresenter;
import com.qun.weichat.presenter.ConversationPresenterImpl;
import com.qun.weichat.view.activity.ChatActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.OnConversationClickListener {

    private RecyclerView mRecyclerView;
    private ConversationPresenter mConversationPresenter;

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
    }

    @Override
    public void onInitConversation(List<EMConversation> emConversationList) {
        ConversationAdapter conversationAdapter = new ConversationAdapter(emConversationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(conversationAdapter);
        conversationAdapter.setOnConversationClickListener(this);
    }

    @Override
    public void onConversationClick(EMConversation conversation, int position) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("username", conversation.getLastMessage().getUserName());
        startActivity(intent);
    }
}
