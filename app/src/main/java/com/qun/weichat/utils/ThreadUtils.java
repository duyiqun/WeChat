package com.qun.weichat.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Qun on 2017/5/13.
 */

public class ThreadUtils {

    private static Executor sExecutor = Executors.newCachedThreadPool();
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnSubThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    /**
     * 该任务绝对保证是主线程被调用
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable) {

        //在handler所绑定的线程中执行任务
        sHandler.post(runnable);
    }
}
