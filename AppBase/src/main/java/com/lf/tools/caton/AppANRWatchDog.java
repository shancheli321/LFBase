package com.lf.tools.caton;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class AppANRWatchDog {

    private final static String TAG = "MyANRWatchDog---------";

    //一个标志
    private static final int TICK_INIT_VALUE = 0;

    private volatile int mTick = TICK_INIT_VALUE;

    //任务执行间隔
    public final int DELAY_TIME = 4000;

    //UI线程Handler对象
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //性能监控线程
    private HandlerThread mWatchDogThread = new HandlerThread("WatchDogThread");

    //性能监控线程Handler对象
    private Handler mWatchDogHandler;

    //定期执行的任务
    private Runnable mDogRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mHandler) {
                Log.e(TAG, "handler is null");
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {//UI线程中执行
                    mTick++;
                }
            });
            try {
                //线程休眠时间为检测任务的时间间隔
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //当mTick没有自增时，表示产生了卡顿，这时打印UI线程的堆栈
            if (TICK_INIT_VALUE == mTick) {
                StringBuilder sb = new StringBuilder();
                StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
                for (StackTraceElement s : stackTrace) {
                    sb.append(s.toString() + "\n");
                }
                Log.e(TAG, sb.toString());
            } else {
                mTick = TICK_INIT_VALUE;
            }
            mWatchDogHandler.post(mDogRunnable);
        }
    };

    /**
     * 卡顿监控工作start方法
     */
    public void startWork(){
        mWatchDogThread.start();
        mWatchDogHandler = new Handler(mWatchDogThread.getLooper());
        mWatchDogHandler.post(mDogRunnable);
    }
}
