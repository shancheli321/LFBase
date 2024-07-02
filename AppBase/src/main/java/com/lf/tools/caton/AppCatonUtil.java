package com.lf.tools.caton;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import com.lf.base.single.AppSingle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
    1. 自动化卡顿检测方案

    2. ANR
         导出文件： adb pull data/anr/traces.txt

        通过FileObserver监控文件变化
        ARN-watchDog

        IPC 问题检测

    3. 界面秒开
        Lancet  AOP框架

        耗时盲区监测

    4. 你是怎么做卡顿优化的

        第一阶段：通过系统工具定位 通过异步优化解决
        第二阶段： 自动化卡顿方案优化
        第三阶段：线上监控和线下线下监测工具建设

    5. 怎么自动化的获取卡顿信息
         mLooping.println
         高频采集 找出重复堆栈
 */


public class AppCatonUtil {

    private static final String TAG = "AppCatonUtil=============";

    private volatile static AppCatonUtil appCatonUtil;

    private Handler mHandler;

    //方法耗时的卡口,500毫秒
    private int timeSpace = 500;

    //存放一个msg周期的卡顿堆栈信息，防止重复打印
    private Set<String> mBlockStackTrace;

    private AppCatonUtil() {
        HandlerThread logThread = new HandlerThread("AppCatonUtil");
        logThread.start();
        mHandler = new Handler(logThread.getLooper());
        mBlockStackTrace = Collections.synchronizedSet(new HashSet<String>());
    }


    public static AppCatonUtil getInstance() {
        if (appCatonUtil == null) {
            synchronized (AppSingle.class) {
                if (appCatonUtil == null) {
                    appCatonUtil = new AppCatonUtil();
                }
            }
        }
        return appCatonUtil;
    }

    public void init(boolean isOpen) {
        if (isOpen) {
            Looper.getMainLooper().setMessageLogging(new Printer() {

                //分发和处理消息开始前的log
                private static final String START = ">>>>> Dispatching";

                //分发和处理消息结束后的log
                private static final String END = "<<<<< Finished";

                @Override
                public void println(String x) {
                    if (x.startsWith(START)) {

                        //开始计时
                        startMonitor();
                    }
                    if (x.startsWith(END)) {
                        //结束计时
                        removeMonitor();
                    }
                }
            });
        }
    }

    public void setTimeSpace(int timeSpace) {
        this.timeSpace = timeSpace;
    }

    private Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            //继续检测
            startMonitor();

            //打印出执行的耗时方法的栈消息
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();

            StringBuilder sb = new StringBuilder();

            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString());
                sb.append("\n");
            }

            String s = sb.toString();
            if (!mBlockStackTrace.contains(s)) {
                mBlockStackTrace.add(s);

                Log.e(TAG," ");
                Log.e(" ", s);
                Log.e(TAG," ");
            }
        }
    };


    /**
     * 开始计时
     */
    private void startMonitor() {
        mHandler.postDelayed(mLogRunnable, this.timeSpace);
    }

    /**
     * 停止计时
     */
    private void removeMonitor() {
        mHandler.removeCallbacks(mLogRunnable);
        mBlockStackTrace.clear();
    }
}
