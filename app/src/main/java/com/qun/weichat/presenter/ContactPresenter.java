package com.qun.weichat.presenter;

/**
 * Created by Qun on 2017/5/23.
 */

public interface ContactPresenter {
    void initContacts();
    void onUpdate();
    void delete(String username);
}
