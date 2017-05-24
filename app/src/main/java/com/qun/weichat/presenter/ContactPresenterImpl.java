package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.db.DBUtils;
import com.qun.weichat.utils.ThreadUtils;
import com.qun.weichat.view.fragment.ContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Qun on 2017/5/23.
 */

public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;
    private List<String> contactsList = new ArrayList<>();

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
        final String currentUser = EMClient.getInstance().getCurrentUser();
        List<String> contacts = DBUtils.getContacts(currentUser);
        contactsList.clear();
        contactsList.addAll(contacts);
        sortContactList();
        mContactView.onInit(contactsList);

        updateFromServer(currentUser);
    }

    private void updateFromServer(final String currentUser) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    DBUtils.updateContacts(currentUser, contactsFromServer);
                    contactsList.clear();
                    contactsList.addAll(contactsFromServer);
                    sortContactList();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdate(true, "success");
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onUpdate(false, e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onUpdate() {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        updateFromServer(currentUser);
    }

    private void sortContactList() {
        //给集合排序
        Collections.sort(contactsList, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
    }
}
