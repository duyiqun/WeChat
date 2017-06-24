package com.qun.weichat.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Qun on 2017/5/26.
 */

public interface ChatPresenter {
    void init(String username, int pageSize);
    void sendTextMessage(String msg, String username);
    void receiveMsg(EMMessage emMessage);
    void loadMoreMsg(int pageSize);
    void sendImageMsg(String imagePath, String username);
}
