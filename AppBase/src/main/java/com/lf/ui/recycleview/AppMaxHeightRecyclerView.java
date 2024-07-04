package com.lf.ui.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lf.base.R;

/**
 * @description 可以指定最大高度的列表
 */

public class AppMaxHeightRecyclerView extends RecyclerView {

    private int maxHeight = 0;
    public AppMaxHeightRecyclerView(Context context) {
        super(context);
    }

    public AppMaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context,attrs);
    }

    public AppMaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context,attrs);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    private void initialize(Context context, @Nullable AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppMaxHeightRecyclerView);
        maxHeight = typedArray.getLayoutDimension(R.styleable.AppMaxHeightRecyclerView_maxHeightRecyclerView,maxHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight >0){
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
