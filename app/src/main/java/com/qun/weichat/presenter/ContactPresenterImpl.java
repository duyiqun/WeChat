package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.utils.ThreadUtils;
import com.qun.weichat.view.fragment.ContactView;

import java.util.List;

/**
 * Created by Qun on 2017/5/23.
 */

public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;

    public ContactPresenterImpl(ContactView contactView) {
        this.mContactView = contactView;
    }

    /**
     * 获取当前用户的好友
     * 1. 先从本地数据库缓存中获取好友，返回给View层
     * 2. 开启子线程去网络上获取最新的好友
     * 3. 将最新的好友列表缓存到本地数据库
     * 4. 更新View层
     */
    @Override
    public void initContacts() {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
