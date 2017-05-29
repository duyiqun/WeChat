package com.qun.weichat.view.activity;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Qun on 2017/5/26.
 */

public interface ChatView {
    void onInit(List<EMMessage> emMessageList);
    void onSendMsg(EMMessage message);
}
