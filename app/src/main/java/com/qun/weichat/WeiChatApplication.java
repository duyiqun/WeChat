package com.qun.weichat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.qun.weichat.db.DBUtils;
import com.qun.weichat.event.ContactUpdateEvent;
import com.qun.weichat.view.activity.ChatActivity;
import com.qun.weichat.view.activity.LoginActivity;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Qun on 2017/5/11.
 */

public class WeiChatApplication extends Application {

    private static final String TAG = "WeiChatApplication";
    private SoundPool mSoundPool;
    private int mDuanSound;
    private int mYuluSound;
    private NotificationManager mNotificationManager;
    private HashMap<String, Integer> mHashMap = new HashMap();
    //    private List<BaseActivity> mBaseActivities = new ArrayList<>();
    private List<Activity> mBaseActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMob();
        initAVOSCloud();
        initDB();
        initSoundPool();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initActivityLifeCycleListener();

        LeakCanary.install(this);
    }

    private void initActivityLifeCycleListener() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                removeActivity(activity);
            }
        });
    }

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetFileDescriptor duanAFD = getAssets().openFd("duan.mp3");
            AssetFileDescriptor yuluAFD = getAssets().openFd("yulu.mp3");
            mDuanSound = mSoundPool.load(duanAFD, 1);
            mYuluSound = mSoundPool.load(yuluAFD, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //监听通讯录变化
        initContactListener();
        //添加消息监听
        initMessageListener();
        //添加连接监听
        initConnectListener();
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

    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //收到消息
                Log.d(TAG, "onMessageReceived: " + list.get(0).toString());
                EventBus.getDefault().post(list.get(0));

                boolean isRunBackground = isRunInBackGround();
                if (isRunBackground) {
                    mSoundPool.play(mYuluSound, 1, 1, 0, 0, 1);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    sendNotification(list.get(0));
                } else {
                    mSoundPool.play(mDuanSound, 1, 1, 0, 0, 1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                //消息状态变动
            }
        });
    }

    private void sendNotification(EMMessage emMessage) {
        String userName = emMessage.getUserName();
        int id = 1;
        if (mHashMap.containsKey(userName)) {
            id = mHashMap.get(userName);
        } else {
            mHashMap.put(userName, mHashMap.size() + 1);
            id = mHashMap.size();
        }
        String msg = "";
        switch (emMessage.getType()) {
            case TXT:
                EMTextMessageBody textMessageBody = (EMTextMessageBody) emMessage.getBody();
                msg = textMessageBody.getMessage();
                break;
            case IMAGE:
                msg = "【图片】";
                break;
            default:
                msg = "未知类型";
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.largmessage);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("username", userName);
        Intent[] intents = {mainIntent, chatIntent};

        PendingIntent pendingIntent = PendingIntent.getActivities(this, id, intents, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.message)
                .setLargeIcon(bitmap)
                .setContentTitle("你有新消息")
                .setContentText(msg)
                .setContentInfo(userName)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                //设置延时意图，就是指定点击通知时的动作
                .setContentIntent(pendingIntent)
                .build();

        mNotificationManager.notify(id, notification);
    }

    private boolean isRunInBackGround() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        /**
         * 获取手机中当前运行的任务栈
         */
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        //获取最前面的任务栈
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        //获取这个任务栈中最顶端的Activity
        ComponentName topActivity = runningTaskInfo.topActivity;
        //判断这个Activity的包名是否跟我们的包名一致
        return !topActivity.getPackageName().equals(getPackageName());
    }

    private void initConnectListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                switch (error) {
                    case EMError.USER_LOGIN_ANOTHER_DEVICE:
                        //先将app内的所有的Activity给清空（finish）
                        clearAllActivity();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//    public void addActivity(BaseActivity activity) {
//        if (!mBaseActivities.contains(activity)) {
//            mBaseActivities.add(activity);
//        }
//    }
//
//    public void removeActivity(BaseActivity activity) {
//        mBaseActivities.remove(activity);
//    }
//
//    private void clearAllActivity() {
//        for (int i = 0; i < mBaseActivities.size(); i++) {
//            BaseActivity baseActivity = mBaseActivities.get(i);
//            baseActivity.finish();
//        }
//        mBaseActivities.clear();
//    }

    public void addActivity(Activity activity) {
        if (!mBaseActivities.contains(activity)) {
            mBaseActivities.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        mBaseActivities.remove(activity);
    }

    private void clearAllActivity() {
        for (int i = 0; i < mBaseActivities.size(); i++) {
            Activity activity = mBaseActivities.get(i);
            activity.finish();
        }
        mBaseActivities.clear();
    }
}
