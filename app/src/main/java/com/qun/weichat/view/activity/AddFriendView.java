package com.qun.weichat.view.activity;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by Qun on 2017/5/24.
 */

public interface AddFriendView {
    void onSearch(boolean isSuccess, String msg, List<AVUser> userList, List<String> myFriendList);
    void onAddFiend(boolean isSuccess, String msg, String username);
}
