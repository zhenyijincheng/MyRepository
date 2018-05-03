package com.toy.qiaodata.callrecoder

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by YANG on 2018/3/13.
 */
class MyView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i("MyView", "dispatchTouchEvent start")
        val b = super.dispatchTouchEvent(event)
        Log.i("MyView", "dispatchTouchEvent end return: " + b)
        return false
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val b = true
        Log.i("MyView", "onTouchEvent return: " + b)
        return b
    }
}