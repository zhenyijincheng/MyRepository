package com.toy.qiaodata.callrecoder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by YANG on 2018/3/7.
 */

public class MyViewGroup extends FrameLayout {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("MyViewGroup","dispatchTouchEvent start");
//        boolean b = false;
        boolean b = super.dispatchTouchEvent(event);
        Log.i("MyViewGroup","dispatchTouchEvent end return: " + b);
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i("MyViewGroup","onInterceptTouchEvent start");
        boolean b = super.onInterceptTouchEvent(event);
        Log.i("MyViewGroup","onInterceptTouchEvent end return: " + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("MyViewGroup","onTouchEvent start");
        boolean b = false;
        Log.i("MyViewGroup","onTouchEvent end return: " + b);
        return b;
    }
}
