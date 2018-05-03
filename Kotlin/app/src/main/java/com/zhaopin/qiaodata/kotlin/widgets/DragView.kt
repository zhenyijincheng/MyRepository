package com.zhaopin.qiaodata.kotlin.widgets

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v4.widget.ViewDragHelper
import android.support.v4.widget.ViewDragHelper.create
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.zhaopin.qiaodata.kotlin.R
import kotlinx.android.synthetic.main.layout_drag_view.view.*

/**
 *
 * Created by YANG on 2017/12/18.
 */

class DragView : FrameLayout{

    /**-----------------------常量声明区------------------------------*/
    private val DRAG_TOP_MARGING = 100//最高点距顶部的距离
    private var DRAG_RANGE = 640//可以滑动的范围
    private val DRAG_REBOUND_RANGE = 100 //在最高点和最低点的弹性距离
    private val FLING_MIN_DISTANCE = 20//mListView  滑动最小距离
    private val FLING_MIN_VELOCITY = 200//mListView 滑动最大速度
    /**-----------------------UI控件成员变量声明区---------------------*/
    var backgroundView:BackgroundView? = null
    /**-----------------------普通成员变量声明区-----------------------*/
    private val mViewDragHelper:ViewDragHelper
    var currentTop = DRAG_TOP_MARGING
    private var onDragPositionListener: OnDragPositionListener? = null
    private var onDragScrollListener: OnDragScrollListener? = null
    /**-----------------------内部接口声明区--------------------------*/

    interface OnDragPositionListener {
        fun onTop()
        fun onBottom()
    }
    interface OnDragScrollListener {
        fun onScroll(Top: Int)
    }
    /**-----------------------初始化相关方法区------------------------*/
    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,-1)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mViewDragHelper = create(this,1.0f,object :ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
                return child?.id == fl_conten.id
            }

            override fun onViewPositionChanged(changedView: View?, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                if (top == DRAG_TOP_MARGING && currentTop > top) {
                    onDragPositionListener?.onTop()
                }

                if (top == getDragBottomMargin() && currentTop < top) {
                    onDragPositionListener?.onBottom()
                }
                currentTop = top
                onDragScrollListener?.onScroll(top)
                Log.i("TOP",top.toString())
                backgroundView?.onScroll(top)

            }

            override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
                var mTop = top

                val min_top = DRAG_TOP_MARGING - DRAG_REBOUND_RANGE
                val max_top = getDragBottomMargin() + DRAG_REBOUND_RANGE

                if (top <= min_top) {
                    mTop = min_top
                }
                if (top >= max_top) {
                    mTop = max_top
                }
                return mTop
            }
            override fun getViewVerticalDragRange(child: View?): Int {
                val i = measuredHeight - child!!.measuredHeight
                return i
            }

            override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                val dragToTop: Int//
                if (Math.abs(yvel) > FLING_MIN_VELOCITY) {//是滑动
                    if (yvel > 0) {//向下划
                        dragToTop = getDragBottomMargin()
                    } else {//向上划
                        dragToTop = DRAG_TOP_MARGING
                    }
                } else {
                    if (currentTop < getMiddlePoint()) {
                        dragToTop = DRAG_TOP_MARGING
                    } else {
                        dragToTop = getDragBottomMargin()
                    }
                }
                setDragMarginTop(dragToTop)
                invalidate()
            }
        })
        backgroundView = BackgroundView(context, -200,DRAG_TOP_MARGING - DRAG_REBOUND_RANGE,DRAG_RANGE + DRAG_REBOUND_RANGE * 2,150)
    }
    /**-----------------------事件响应方法区--------------------------*/
    fun setOnDragScrollListener(onRedScrollListener: OnDragScrollListener) {
        this.onDragScrollListener = onRedScrollListener
    }
    fun setOnDragPositionListener(onDragPositionListener: OnDragPositionListener) {
        this.onDragPositionListener = onDragPositionListener
    }
    /**-----------------------普通逻辑方法区--------------------------*/
    private fun setDragMarginTop(top: Int) {
        mViewDragHelper.captureChildView(fl_conten, 0)
        mViewDragHelper.settleCapturedViewAt(0, top)
    }
    /**
     * 中间点
     */
    fun getMiddlePoint(): Int {
        return (DRAG_TOP_MARGING + DRAG_RANGE) / 2
    }
    /**
     * 最低点
     */
    fun getDragBottomMargin():Int {
        return DRAG_RANGE + DRAG_TOP_MARGING
    }
    /**-----------------------重载的逻辑方法区------------------------*/
    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        fl_conten.top = currentTop
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mViewDragHelper.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val flContent = View.inflate(context, R.layout.layout_drag_view, null)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.topMargin = DRAG_TOP_MARGING
        flContent.layoutParams = layoutParams
        addView(backgroundView)
        addView(flContent)
    }
    /**-----------------------内部类声明区---------------------------*/
}
