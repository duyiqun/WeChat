package com.qun.weichat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.db.DBUtils;
import com.qun.weichat.event.ContactUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Qun on 2017/5/11.
 */

public class WeiChatApplication extends Application {

    private static final String TAG = "WeiChatApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMob();
        initAVOSCloud();
        initDB();
    }

    private void initDB() {
        DBUtils.init(this);
    }

    private void initEaseMob() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = i.next();
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initAVOSCloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "wl0RzoKYQBVL7X4uBoHuJxI8-gzGzoHsz", "wbs6MPcKfYIHz8etg9wPxHX9");
        AVOSCloud.setDebugLogEnabled(true);

        //监听通讯录变化
        initContactListener();
    }

    private void initContactListener() {

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAdded(String username) {
                Log.d(TAG, "onContactAdded: " + username);
                EventBus.getDefault().post(new ContactUpdateEvent(username, true));
            }

            @Override
            public void onContactDeleted(String username) {
                Log.d(TAG, "onContactDeleted: " + username);
                EventBus.getDefault().post(new ContactUpdateEvent(username, false));
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //自动同意
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    Log.d(TAG, "onContactInvited: " + username + "/" + reason);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String username) {
                Log.d(TAG, "onFriendRequestAccepted: " + username);
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                Log.d(TAG, "onFriendRequestDeclined: " + username);
            }
        });
    }
}
