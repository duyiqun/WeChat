package com.qun.weichat.utils;

import android.text.TextUtils;

/**
 * Created by Qun on 2017/5/13.
 */

public class StringUtils {

    private static final String REGEXP_USERNAME = "^[a-zA-Z]\\w{2,15}$";
    private static final String REGEXP_PWD = "^[0-9]{3,16}$";

    //字母开头，长度为[3,16]
    public static boolean checkUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        return username.matches(REGEXP_USERNAME);
    }

    //纯数字密码，长度[3,16]
    public static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        return pwd.matches(REGEXP_PWD);
    }

    public static String getInitial(String contact) {
        if (TextUtils.isEmpty(contact)) {
            return "搜";
        }
        return contact.substring(0, 1).toUpperCase();
    }
}
