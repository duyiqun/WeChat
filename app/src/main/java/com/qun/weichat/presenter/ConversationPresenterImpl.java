package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.qun.weichat.view.fragment.ConversationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Qun on 2017/6/25.
 */

public class ConversationPresenterImpl implements ConversationPresenter {

    private ConversationView mConversationView;
    private List<EMConversation> mEMConversationList = new ArrayList<>();

    public ConversationPresenterImpl(ConversationView conversationView) {
        this.mConversationView = conversationView;
    }

    @Override
    public void initConversation() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        mEMConversationList.clear();
        mEMConversationList.addAll(allConversations.values());
        Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
            }
        });
        mConversationView.onInitConversation(mEMConversationList);
    }
}
