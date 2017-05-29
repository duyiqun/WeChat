package com.qun.weichat.presenter;

/**
 * Created by Qun on 2017/5/26.
 */

public interface ChatPresenter {
    void init(String username, int pageSize);
    void sendTextMessage(String msg, String username);
}
