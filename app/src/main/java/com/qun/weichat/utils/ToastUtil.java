package com.qun.weichat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Qun on 2017/5/13.
 */

public class ToastUtil {

    private static Toast sToast;

    public static void showMsg(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
