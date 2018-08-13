package com.toy.qiaodata.redbubble.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewTreeObserver

/**
 * create by YANG on 2018/7/11 14:46
 */
class BubbleView : View, ViewTreeObserver.OnDrawListener {


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var paint: Paint
    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f
    private var radius: Float = 25f
    private val onChangeListener:OnChangeListener

    init {
        radius = 25f
        if(isInEditMode) {
            centerX = radius
            centerY = radius
        }
        paint = Paint()
        paint.color = Color.parseColor("#FF0000")
        viewTreeObserver.addOnDrawListener(this)

        onChangeListener = object : OnChangeListener{
            override fun onRadiusChange(r: Float) {
                radius = r
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec((radius * 2).toInt(),MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec((radius * 2).toInt(),MeasureSpec.UNSPECIFIED))
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("bubble", "x:${event.x} y:${event.y}")
        if (event.action == ACTION_DOWN) {
            var parentView = parent
            while (parentView != null) {
                if (parentView !is BubbleParent) {
                    parentView = parentView.parent
                } else {
                    parentView.setBeginTouch(left + radius, top + radius,radius,onChangeListener)
                    break
                }
            }
        } else if (event.action == ACTION_UP) {
            performClick()
        }
        return false
    }

    override fun onDraw() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, paint)
        if (centerX != 0f || centerY != 0f) {
            viewTreeObserver.removeOnDrawListener(this)
        }
    }

    interface OnChangeListener {
        public fun onRadiusChange(radius: Float)
    }

}

