package com.lf.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.lf.base.R;

/**
 * @date: 2024/6/4
 */
public class AppCustomDialog extends Dialog implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private Context context;
    private TextView mTvTitle;
    private TextView mMessageView;
    private TextView btnLeft, btnRight, mBoxDescView;
    private LinearLayout llBox;
    private CheckBox mCheckBoxView;
    private OnClickListener listener;
    private OnCheckBoxChangeListener checkedChangeListener;

    public AppCustomDialog(Context context) {
        this(context, R.style.AppCustomDialog);
    }

    public AppCustomDialog(Context context, int themeResId) {

        super(context, themeResId);

        this.context = context;

        setCancelable(true);

        setCanceledOnTouchOutside(true);

        init();
    }

    public void setOnClickListener(OnClickListener listener) {

        this.listener = listener;
    }

    public void setOnCheckChangeListener(OnCheckBoxChangeListener checkedChangeListener) {

        this.checkedChangeListener = checkedChangeListener;
    }

    private void init() {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.app_custom_dialog, null);
        setContentView(view);

        mTvTitle = view.findViewById(R.id.tv_title);
        mMessageView = view.findViewById(R.id.tv_content);
        btnLeft = view.findViewById(R.id.tv_cancel);
        btnRight = view.findViewById(R.id.tv_ok);
        llBox = view.findViewById(R.id.ll_box);
        mBoxDescView = view.findViewById(R.id.tv_box_desc);
        mCheckBoxView = view.findViewById(R.id.check_box);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        mCheckBoxView.setOnCheckedChangeListener(this);
    }

    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    public void setLeftMessage(String message) {
        btnLeft.setText(message);
    }

    public void setLeftMessage(String message, boolean isShowNegative) {

        btnLeft.setText(message);
        btnRight.setVisibility(isShowNegative ? View.VISIBLE : View.GONE);
    }

    public void setRightMessage(String message) {
        btnRight.setText(message);
    }

    public void setRightMessage(String message, boolean isShowPositive) {

        btnRight.setText(message);
        btnLeft.setVisibility(isShowPositive ? View.VISIBLE : View.GONE);
    }

    public void setLeftTextColor(int color) {
        btnLeft.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        btnRight.setTextColor(color);
    }

    public void setBoxDescMessage(String message) {
        mBoxDescView.setText(message);
    }

    public void setBoxDescTextColor(int color) {
        mBoxDescView.setTextColor(color);
    }

    public void setBoxDescMessage(String message, boolean isShowBox) {

        mBoxDescView.setText(message);
        llBox.setVisibility(isShowBox ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (checkedChangeListener != null) {

            checkedChangeListener.onCheckChanged(isChecked);
        }
    }

    public TextView getPositiveBtn() {

        return btnLeft;
    }

    public TextView getNegativeBtn() {

        return btnRight;
    }

    public static class Builder {

        private Context mContext;
        private int mThemeId;
        private CharSequence mTitle;
        private CharSequence mMessage;

        private CharSequence mLeftButtonText;
        private CharSequence mRightButtonText;
        private boolean isRightButtonGone = false;
        private boolean isLeftButtonGone = false;

        private DialogInterface.OnClickListener mRightButtonListener;
        private DialogInterface.OnClickListener mLeftButtonListener;

        private int mLeftButtonTextColor;
        private int mRightButtonTextColor;

        private CharSequence mBoxDesc;
        private CheckBox mCheckBox;
        private OnCheckBoxChangeListener mCheckBoxListener;
        private int mBoxDescTextColor;

        private int mCheckBoxGrovity;
        private int mMessageGrovity;

        // 是否点击外部区域消失
        private boolean mCanceledOnTouchOutside;
        // 是否返回键消失
        private boolean mCancelable;

        public Builder(Context context) {

            this.mContext = context;
        }

        public Builder setThemeId(int themeId) {

            this.mThemeId = themeId;
            return this;
        }

        public Builder setTitle(@StringRes int titleResId) {

            return setTitle(mContext.getString(titleResId));
        }

        public Builder setTitle(CharSequence title) {

            this.mTitle = title;

            return this;
        }

        public Builder setMessage(@StringRes int messageResId) {

            return setMessage(mContext.getString(messageResId));
        }

        public Builder setMessage(CharSequence message) {

            this.mMessage = message;
            return this;
        }

        public Builder setLeftButtonText(@StringRes int resId) {

            return setLeftButtonText(mContext.getString(resId));
        }

        public Builder setLeftButtonText(CharSequence leftButtonText) {

            this.mLeftButtonText = leftButtonText;
            return this;
        }

        public Builder setLeftButtonListener(DialogInterface.OnClickListener listener) {

            this.mLeftButtonListener = listener;
            return this;
        }

        public Builder setRightButtonText(@StringRes int resId) {

            return setRightButtonText(mContext.getString(resId));
        }

        public Builder setRightButtonText(CharSequence rightButtonText) {

            this.mRightButtonText = rightButtonText;
            return this;
        }

        public Builder setRightButtonGone() {
            this.isRightButtonGone = true;
            return this;
        }

        public Builder setLeftButtonGone() {
            this.isLeftButtonGone = true;
            return this;
        }

        public Builder setRightButtonListener(DialogInterface.OnClickListener listener) {

            this.mRightButtonListener = listener;
            return this;
        }

        public Builder setLeftButtonTextColor(@ColorRes int leftButtonTextColor) {

            this.mLeftButtonTextColor = leftButtonTextColor;
            return this;
        }

        public Builder setRightButtonTextColor(@ColorRes int rightButtonTextColor) {

            this.mRightButtonTextColor = rightButtonTextColor;
            return this;
        }

        public Builder setBoxDesc(CharSequence mBoxDesc) {

            this.mBoxDesc = mBoxDesc;
            return this;
        }

        public Builder setCheckBoxListener(OnCheckBoxChangeListener listener) {

            this.mCheckBoxListener = listener;
            return this;
        }

        public Builder setBoxDescTextColor(@ColorRes int mBoxDescTextColor) {

            this.mBoxDescTextColor = mBoxDescTextColor;
            return this;
        }

        public Builder setCheckBoxGrovity(int grovity) {
            this.mCheckBoxGrovity = grovity;
            return this;
        }

        public Builder setMessageGrovity(int grovity) {
            this.mMessageGrovity = grovity;
            return this;
        }

        public Builder setCanceledOnTouchOutSide(Boolean result){
            this.mCanceledOnTouchOutside = result;
            return this;
        }

        public Builder setCancelable(Boolean result){
            this.mCancelable = result;
            return this;
        }

        public AppCustomDialog build() {

            AppCustomDialog dialog;

            if (this.mThemeId != 0) {

                dialog = new AppCustomDialog(mContext, this.mThemeId);
            } else {

                dialog = new AppCustomDialog(mContext);
            }

            if (this.mLeftButtonListener == null) {

                this.mLeftButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
            }

            if (this.mRightButtonListener == null) {

                this.mRightButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
            }

            dialog.btnLeft.setOnClickListener(v -> {
                dialog.btnLeft.setEnabled(false);
                mLeftButtonListener.onClick(dialog, 0);
            });
            dialog.btnRight.setOnClickListener(v -> {
                dialog.btnRight.setEnabled(false);
                mRightButtonListener.onClick(dialog, 1);
            });

            if (!TextUtils.isEmpty(mLeftButtonText)) {
                dialog.btnLeft.setText(mLeftButtonText);
            } else {
                dialog.btnLeft.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mRightButtonText)) {
                dialog.btnRight.setText(mRightButtonText);
            } else {
                dialog.btnRight.setVisibility(View.GONE);
            }
            if (isRightButtonGone) {
                dialog.btnRight.setVisibility(View.GONE);
            }
            if (isLeftButtonGone) {
                dialog.btnLeft.setVisibility(View.GONE);
            }

            if (mLeftButtonTextColor != 0) {
                dialog.btnLeft.setTextColor(mContext.getColor(mLeftButtonTextColor));
            }

            if (mRightButtonTextColor != 0) {
                dialog.btnRight.setTextColor(mContext.getColor(mRightButtonTextColor));
            }

            if (!TextUtils.isEmpty(mTitle)) {

                dialog.mTvTitle.setVisibility(View.VISIBLE);
                dialog.mTvTitle.setText(mTitle);
            } else {

                dialog.mTvTitle.setVisibility(View.GONE);
                dialog.mTvTitle.setText(null);
            }

            if (mMessageGrovity != 0) {
                dialog.mMessageView.setGravity(mMessageGrovity);
            }

            if (!TextUtils.isEmpty(mMessage)) {

                dialog.mMessageView.setVisibility(View.VISIBLE);
                dialog.mMessageView.setText(mMessage);
            } else {

                dialog.mMessageView.setVisibility(View.GONE);
                dialog.mMessageView.setText(null);
            }
            //只有标题，没有内容
            if (TextUtils.isEmpty(mMessage)&& !TextUtils.isEmpty(mTitle)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dialog.mTvTitle.getLayoutParams();
                layoutParams.setMargins(0,20,0,20);
            }
            if (!TextUtils.isEmpty(mMessage)&& TextUtils.isEmpty(mTitle)){
                dialog.mMessageView.setVisibility(View.GONE);
                dialog.mMessageView.setText(null);
                dialog.mTvTitle.setVisibility(View.VISIBLE);
                dialog.mTvTitle.setText(mMessage);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dialog.mTvTitle.getLayoutParams();
                layoutParams.setMargins(0,20,0,20);
            }

            if (!TextUtils.isEmpty(mBoxDesc) && this.mCheckBoxListener != null) {

                dialog.llBox.setVisibility(View.VISIBLE);
                dialog.mBoxDescView.setText(mBoxDesc);
                dialog.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> mCheckBoxListener.onCheckChanged(isChecked));

                if (mBoxDescTextColor != 0) {
                    dialog.mBoxDescView.setTextColor(mContext.getColor(mBoxDescTextColor));
                }

                if (mCheckBoxGrovity != 0) {
                    dialog.llBox.setGravity(mCheckBoxGrovity);
                }

            } else {

                dialog.llBox.setVisibility(View.GONE);
                dialog.mBoxDescView.setText(null);
            }
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            dialog.setCancelable(mCancelable);
            return dialog;
        }
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            if (v.getId() == R.id.tv_cancel) {

                listener.onLeftClick(v);
            } else if (v.getId() == R.id.tv_ok) {

                listener.onRightClick(v);
            }
        }
        dismiss();
    }

    public interface OnClickListener {

        void onLeftClick(View view);

        void onRightClick(View view);
    }

    public interface OnCheckBoxChangeListener {

        void onCheckChanged(boolean isChecked);
    }

}
