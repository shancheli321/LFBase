package com.lf.ui.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lf.base.R;

public class AppProgressUtil {

    public static AppProgressDialog showLoading(Context context, CharSequence message) {
        AppProgressDialog dialog = new AppProgressDialog(context, R.style.ProgressHUD);
        dialog.setContentView(R.layout.app_progress_view);
        TextView txt = (TextView) dialog.findViewById(R.id.message);
        txt.setText(message);
        dialog.setCancelable(false);//Cancelable:可撤销
        dialog.setOnCancelListener(null);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    public static class AppProgressDialog extends Dialog {

        public AppProgressDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
            if (imageView != null) {
                AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
                spinner.start();
            }
        }
    }
}
