package com.lf.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.lf.base.R;
import com.lf.base.manager.AppActivityManager;

public class AppBaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_app_base);

        AppActivityManager.getActivityManager().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppActivityManager.getActivityManager().popActivity(this);
    }

    /**
     * 启动一个Activity
     * @param activity  需要启动的Activity的Class
     */
    public void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    /**
     *  findViewById 不需要再去强转
     */
    public <T extends View> T viewById(@IdRes int resId) {
        return (T) super.findViewById(resId);
    }


    /**
     * 监控一个Activity是否载完毕；
     * 在Activity加载后进行一些操作，如获取手机屏幕的高度和宽度；
     * 当Activity挂起或恢复时，可以在方法内进行一些数据的保存或恢复的操作；
     *
     * onWindowsFocusChanged()就是指当前的Activity的Windows(窗口)获取或者失去焦点时这个方法就会被调用，并且当回调这个方法时，Activity是完全可见的
     * 在Activity生命周期中，onStart(), onResume(), onCreate()都不是布局visible的时间点，真正的visible时间点是onWindowFocusChanged()函数被执行时。从onWindowFocusChanged()被执行起，用户可以与应用进行交互了
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    protected void setStatusBar(View statusBar) {
        Window window = getWindow();
        //背景延伸至状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //以下设置状态栏高度
//        View statusBar = findViewById(R.id.statusbar_holder);

//          <View
//        android:id="@+id/statusbar_holder"
//        android:layout_width="match_parent"
//        android:layout_height="0dp"
//        app:layout_constraintTop_toTopOf="parent"
//                />

        ViewGroup.LayoutParams param = statusBar.getLayoutParams();
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        param.height = mContext.getResources().getDimensionPixelSize(resourceId);
        statusBar.setLayoutParams(param);
        statusBar.setBackgroundColor(getResources().getColor(R.color.color_transparent));
    }
}