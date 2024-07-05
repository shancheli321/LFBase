package com.example.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.R;
import com.lf.base.activity.AppBaseActivity;
import com.lf.base.listener.AppClick;
import com.lf.ui.dialog.AppCustomDialog;
import com.lf.ui.imageview.LFCircleImageView;
import com.lf.ui.util.AppProgressUtil;
import com.lf.ui.util.AppToastUtil;

public class MainActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastUtil.showLong( "tost弹窗");
            }
        });

        findViewById(R.id.tv_toast_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastUtil.showSuccess( "tost弹窗");
            }
        });

        findViewById(R.id.tv_toast_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastUtil.showBlack( "tost弹窗");
            }
        });

        findViewById(R.id.tv_toast_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastUtil.showWhite( "tost弹窗");
            }
        });

        findViewById(R.id.tv_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppCustomDialog.Builder(mContext)
                        .setTitle("弹窗")
                        .setMessage("弹窗内容弹窗内容弹窗内容弹窗内容弹窗内容")
                        .setLeftButtonText("很好")
                        .setRightButtonText("good")
                        .setLeftButtonListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setRightButtonListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
            }
        });

        findViewById(R.id.tv_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LFCircleImageView imageView = findViewById(R.id.iv_circle);
        Glide.with(MainActivity.this).load(R.drawable.hugh).into(imageView);
    }
}