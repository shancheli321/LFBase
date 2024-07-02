package com.example.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.R;
import com.lf.base.activity.AppBaseActivity;
import com.lf.base.listener.AppClick;
import com.lf.ui.dialog.AppCustomDialog;
import com.lf.ui.util.AppToastUtil;

public class MainActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastUtil.showLong(mContext, "tost弹窗");
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
    }
}