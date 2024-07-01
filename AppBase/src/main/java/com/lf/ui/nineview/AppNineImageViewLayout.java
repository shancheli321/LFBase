package com.lf.ui.nineview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AppNineImageViewLayout extends ViewGroup {

    protected Context mContext;

    // 默认间距
    private static final float DEFUALT_SPACING = 3f;

    // 默认个数
    private static final int MAX_COUNT = 9;

    // 间距
    private float mSpacing = DEFUALT_SPACING;

    // 设置数据后 显示几行几列
    private int mColumns;
    private int mRows;

    // 总宽度
    private int mTotalWidth;

    // item的宽度
    private int mItemWidth;

    // 是否显示所有图片
    private boolean mIsShowAll = false;

    private List<AppNineImageData> mUrlList = new ArrayList<>();

    public AppNineImageViewLayout(Context context) {
        super(context);
        init(context);
    }

    public AppNineImageViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (getListSize(mUrlList) == 0) {
            setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTotalWidth = right - left;
        mItemWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);


        if (getListSize(mUrlList) != getChildCount()) {
            refresh();
        }

    }

    /**
     * 设置间隔
     *
     * @param spacing
     */
    public void setSpacing(float spacing) {
        mSpacing = spacing;
    }

    /**
     * 设置是否显示所有图片（超过最大数时）
     *
     * @param isShowAll
     */
    public void setIsShowAll(boolean isShowAll) {
        mIsShowAll = isShowAll;
    }

    public void setUrlList(List<AppNineImageData> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        mUrlList.clear();
        mUrlList.addAll(urlList);

        removeAllViews();
    }

    private void refresh() {

        int size = getListSize(mUrlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }

        if (size == 1) {
            String url = mUrlList.get(0).getUrl();
            AppNineImageView imageView = createImageView(0, url);

            //避免在ListView中一张图未加载成功时，布局高度受其他item影响
            LayoutParams params = getLayoutParams();
            params.height = mItemWidth;
            setLayoutParams(params);

            imageView.layout(0, 0, mItemWidth, mItemWidth);

            addView(imageView);

            return;
        }

        generateChildrenLayout(size);

        layoutParams();

        for (int i = 0; i < size; i++) {
            String url = mUrlList.get(i).getUrl();
            AppNineImageView imageView;
            if (!mIsShowAll) {
                if (i < MAX_COUNT - 1) {
                    imageView = createImageView(i, url);
                    layoutImageView(imageView, i, url, false);
                } else { //第9张时
                    if (size <= MAX_COUNT) {//刚好第9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, false);
                    } else {//超过9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, true);
                        break;
                    }
                }
            } else {
                imageView = createImageView(i, url);
                layoutImageView(imageView, i, url, false);
            }
        }
    }

    private void layoutParams() {
        int singleHeight = mItemWidth;

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
        setLayoutParams(params);
    }

    private AppNineImageView createImageView(final int i, final String url) {
        AppNineImageView imageView = new AppNineImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Glide.with(mContext).load(url).into(imageView);
        return imageView;
    }

    /**
     * @param imageView
     * @param url
     * @param showNumFlag 是否在最大值的图片上显示还有未显示的图片张数
     */
    private void layoutImageView(AppNineImageView imageView, int i, String url, boolean showNumFlag) {
        final int singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
        int singleHeight = singleWidth;

        int[] position = findPosition(i);
        int left = (int) ((singleWidth + mSpacing) * position[1]);
        int top = (int) ((singleHeight + mSpacing) * position[0]);
        int right = left + singleWidth;
        int bottom = top + singleHeight;

        imageView.layout(left, top, right, bottom);

        addView(imageView);
        if (showNumFlag) {//添加超过最大显示数量的文本
            int overCount = getListSize(mUrlList) - MAX_COUNT;
            if (overCount > 0) {
                float textSize = 30;
                final TextView textView = new TextView(mContext);
                textView.setText("+" + String.valueOf(overCount));
                textView.setTextColor(Color.WHITE);
                textView.setPadding(0, singleHeight / 2 - getFontHeight(textSize), 0, 0);
                textView.setTextSize(textSize);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(Color.BLACK);
                textView.getBackground().setAlpha(120);

                textView.layout(left, top, right, bottom);
                addView(textView);
            }
        }
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 根据图片个数确定行列数量
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            mRows = 1;
            mColumns = length;
        } else if (length <= 6) {
            mRows = 2;
            mColumns = 3;
            if (length == 4) {
                mColumns = 2;
            }
        } else {
            mColumns = 3;
            if (mIsShowAll) {
                mRows = length / 3;
                int b = length % 3;
                if (b > 0) {
                    mRows++;
                }
            } else {
                mRows = 3;
            }
        }

    }

    private int getListSize(List<AppNineImageData> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

}

