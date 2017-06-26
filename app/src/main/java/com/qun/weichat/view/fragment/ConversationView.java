package com.qun.weichat.view.fragment;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by Qun on 2017/6/25.
 */

public interface ConversationView {
    void onInitConversation(List<EMConversation> emConversationList);

    void onUpdate();
}
