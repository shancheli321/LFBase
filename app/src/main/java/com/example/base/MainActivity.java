package com.example.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.R;
import com.lf.base.activity.AppBaseActivity;
import com.lf.ui.dialog.AppCustomDialog;
import com.lf.ui.imageview.LFCircleImageView;
import com.lf.ui.util.AppDialogUtil;
import com.lf.ui.util.AppLoadingUtil;
import com.lf.ui.util.AppToastUtil;
import com.lxj.xpopup.interfaces.OnConfirmListener;

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

        findViewById(R.id.tv_dialog1).setOnClickListener(new View.OnClickListener() {
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


        findViewById(R.id.tv_dialog2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogUtil.showOK(mContext, "确定弹窗", "春眠不觉晓，处处闻啼鸟", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        AppDialogUtil.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.tv_dialog3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogUtil.showNormal(mContext, "确定弹窗", "春眠不觉晓，处处闻啼鸟", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        AppDialogUtil.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.tv_dialog4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialogUtil.showSingle(mContext, "确定弹窗", "春眠不觉晓，处处闻啼鸟", "俺知道了", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        AppDialogUtil.dismiss();
                    }
                });
            }
        });


        findViewById(R.id.tv_progress1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLoadingUtil.showLoading(mContext);
            }
        });

        findViewById(R.id.tv_progress2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLoadingUtil.showLoadingText(mContext, "我正在加载 请不要着急");
            }
        });

        findViewById(R.id.tv_progress3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLoadingUtil.showWithDelayed(mContext, 2000);
            }
        });

        findViewById(R.id.tv_progress4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLoadingUtil.dismiss();
            }
        });

        LFCircleImageView imageView = findViewById(R.id.iv_circle);
        Glide.with(MainActivity.this).load(R.drawable.hugh).into(imageView);
    }
}