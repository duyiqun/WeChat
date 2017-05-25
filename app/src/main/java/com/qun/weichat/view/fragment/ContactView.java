package com.qun.weichat.view.fragment;

import java.util.List;

/**
 * Created by Qun on 2017/5/23.
 */

public interface ContactView {
    void onInit(List<String> contactsList);
    void onUpdate(boolean isSuccess, String msg);
    void onDelete(boolean isSuccess, String msg, String username);
}
