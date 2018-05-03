package com.zhaopin.qiaodata.kotlin.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * Created by YANG on 2018/3/21.
 */
class MyScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {
    var mLastMotionY:Int = 0
    var mLastMotionX:Int = 0
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val onInterceptTouchEvent = super.onInterceptTouchEvent(ev)
        when(ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = ev.y.toInt()
                mLastMotionX = ev.x.toInt()

            }
            MotionEvent.ACTION_MOVE -> {

                var diffY = mLastMotionY - ev.y.toInt()
                var diffX = mLastMotionX - ev.x.toInt()

                mLastMotionY = ev.y.toInt()
                mLastMotionX = ev.x.toInt()
            }
        }
        return onInterceptTouchEvent
    }
}