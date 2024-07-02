package com.lf.base.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppActivityManager {

    private static List<Activity> activityStack;
    private static AppActivityManager instance;
    private String TAG = AppActivityManager.class.getSimpleName();

    private AppActivityLogInterface appLog;

    private AppActivityManager() {

    }

    public static AppActivityManager getActivityManager() {
        if (instance == null) {
            instance = new AppActivityManager();
        }
        return instance;
    }

    public void popActivity(Activity activity) {
        if (null != activity) {
            if (activityStack != null && activityStack.size() != 0) {
                int index = activityStack.size() - 1;
                Activity tempActivity = activityStack.get(index);
                if (activity.getClass().equals(tempActivity.getClass())) {
                    activity.finish();
                    activityStack.remove(index);
                    appLog.d(TAG,"弹栈：" + activity.toString());
                    activity = null;
                } else {
                    appLog.d(TAG,"弹栈--类型不对应--弹出--" + activity.getClass());
                    appLog.d(TAG,"弹栈--类型不对应--当前--" + tempActivity.getClass());
                }
            } else {
                appLog.d(TAG,"弹栈--栈为空-----" + activity.getClass());
            }
        }
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<>();
            appLog.d(TAG,"栈为空");
        }
        activityStack.add(activity);
        appLog.d(TAG,"入栈：" + activity.toString());
    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() != 0) {
            activity = activityStack.get(activityStack.size() - 1);
        }
        return activity;
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    public int getActivityCount() {
        return activityStack.size();
    }


    public List<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {
        try {
            popAllActivity();
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {    }
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            popAllActivity();
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } catch (Exception e) {

        }
    }

    /**
     * 重启应用
     * @param activity
     */
    public static void restartApp(Activity activity) {
        Intent intent1 = new Intent(activity, getRestartActivityClass(activity));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent1.getComponent() != null) {
            intent1.setAction(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.finish();
        activity.startActivity(intent1);
        killCurrentProcess();
    }


    /**
     * 获取APP初始页面
     * @param context
     * @return
     */
    private static Class<? extends Activity> getRestartActivityClass(Activity context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null && intent.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e("getLauncherActivity", "Failed when resolving the restart activity class via getLaunchIntentForPackage, stack trace follows!", e);
            }
        }
        return null;
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public void setAppLog(AppActivityLogInterface appLog) {
        this.appLog = appLog;
    }

    public interface AppActivityLogInterface {
        public void d(String tag, String log);
    }

}
