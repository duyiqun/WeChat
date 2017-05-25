package com.qun.weichat.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.db.DBUtils;
import com.qun.weichat.utils.ThreadUtils;
import com.qun.weichat.view.activity.AddFriendView;

import java.util.List;

/**
 * Created by Qun on 2017/5/24.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {

    private AddFriendView mAddFriendView;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        this.mAddFriendView = addFriendView;
    }

    @Override
    public void search(String query) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        //select username from _User where username = like '%query%' and username != '我自己';
        AVQuery<AVUser> avQuery = new AVQuery<>("_User");
        //添加查询条件
        avQuery.whereContains("username", query);
        //添加查询条件，不能包含自己的名字
        avQuery.whereNotEqualTo("username", currentUser);
        avQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e != null) {//遇到异常了
                    mAddFriendView.onSearch(false, e.getMessage(), null, null);
                } else if (list == null || list.size() == 0) {//没有找到数据
                    mAddFriendView.onSearch(false, "没有查询到符合条件的结果", null, null);
                } else {//找到数据了
                    List<String> contacts = DBUtils.getContacts(currentUser);
                    mAddFriendView.onSearch(true, "success", list, contacts);
                }
            }
        });
    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username, "想和你交朋友，一起写代码。");
                    //仅仅是邀请发送成功了
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onAddFiend(true, "success", username);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //网络的问题
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onAddFiend(false, e.getMessage(), username);
                        }
                    });
                }
            }
        });
    }
}
