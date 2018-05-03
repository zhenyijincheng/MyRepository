package com.toy.qiaodata.pagelibrary

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View

/**
 * 
 * Created by YANG on 2018/4/26.
 */

class PageView:View {


    /**-----------------------常量声明区------------------------------*/
    /**-----------------------UI控件成员变量声明区------------------e---*/
    /**-----------------------普通成员变量声明区-----------------------*/
    private lateinit var paint:Paint
    private lateinit var lastBitmap:Bitmap
    private lateinit var nextBitmap:Bitmap
    private var centerX = 0f
    private var centerY = 0f
    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f

    private lateinit var nextPageValueAnimator:ValueAnimator
    private lateinit var prePageValueAnimator:ValueAnimator
    private var degree: Float = 0.0f

    private lateinit var camera: Camera

    private lateinit var drawAdapter:DrawAdapter


    /**-----------------------初始化相关方法区------------------------*/
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }



    private fun init() {
        lastBitmap = BitmapFactory.decodeResource(resources, R.mipmap.share_resume_weixin)
        nextBitmap = BitmapFactory.decodeResource(resources,R.mipmap.share_resume_qq)

        paint = Paint()

        nextPageValueAnimator = ValueAnimator.ofFloat(
                0f,
                1f
        )
        nextPageValueAnimator.addUpdateListener {
            val fl = it.animatedValue as Float
            degree = (fl * 180)
            invalidate()
        }
        nextPageValueAnimator.setDuration(1000)
        nextPageValueAnimator.addListener(object :Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
            }
        })

        prePageValueAnimator = ObjectAnimator.ofFloat(1f,0f)
        prePageValueAnimator.setDuration(1000)
        prePageValueAnimator.addListener(object :Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?){
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })
        prePageValueAnimator.addUpdateListener({
            val fl = it.animatedValue as Float
            degree = (fl * 180)
            invalidate()
        })


        camera = Camera()
        camera.setLocation(0f,0f,(-lastBitmap.width / 2).toFloat())

        drawAdapter = object :DrawAdapter{
            override fun drawLastStatyPage(canvas: Canvas) {
                canvas.save()
                canvas.clipRect(0f,0f,centerX,height.toFloat())
                canvas.drawBitmap(lastBitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawLastMovePage(canvas: Canvas) {
                canvas.save()

                camera.save()
                camera.rotateY(degree)
                canvas.translate(centerX,centerY)
                camera.applyToCanvas(canvas)
                canvas.translate(-centerX,-centerY)
                camera.restore()

                canvas.clipRect(centerX,0f,width.toFloat(),height.toFloat())
                canvas.drawBitmap(lastBitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawNextStatyPage(canvas: Canvas) {
                canvas.save()
                canvas.clipRect(centerX,0f,width.toFloat(),height.toFloat())
                canvas.drawBitmap(nextBitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawNextMovePage(canvas: Canvas) {
                canvas.save()

                camera.save()
                camera.rotateY(degree + 180)
                canvas.translate(centerX,centerY)
                camera.applyToCanvas(canvas)
                canvas.translate(-centerX,-centerY)
                camera.restore()

                canvas.clipRect(0f,0f,centerX,height.toFloat())
                canvas.drawBitmap(nextBitmap,mLeft,mTop,paint)
                canvas.restore()
            }
        }
    }


    /**-----------------------生命周期回调方法区（除onCreate()方法外）--*/
    /**-----------------------事件响应方法区--------------------------*/
    /**-----------------------重载方法区--------------------------*/
    override fun onDraw(canvas: Canvas) {
        getLayout()
        drawAdapter.drawLastStatyPage(canvas)
        drawAdapter.drawNextStatyPage(canvas)
        if (degree <= 90) {
            drawAdapter.drawLastMovePage(canvas)
        }
        if (degree in 90.0..180.0) {
            drawAdapter.drawNextMovePage(canvas)
        }
    }


    private var lastX: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == ACTION_DOWN) {
            lastX = event.x
        }
        if (event.action == ACTION_MOVE) {
            if((lastX - event.x) > 0) {
                nextPageValueAnimator.currentPlayTime = nextPageValueAnimator.currentPlayTime + 10
            } else {
                nextPageValueAnimator.currentPlayTime = nextPageValueAnimator.currentPlayTime - 10
            }
            lastX = event.x
        }
        if(event.action == ACTION_UP) {
            nextPageValueAnimator.start()
        }
        return true
    }



    /**-----------------------普通方法区-----------------------*/

    private fun getLayout() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        mLeft = centerX - lastBitmap.width / 2
        mRight = centerX + lastBitmap.width / 2
        mTop = centerY - lastBitmap.height / 2
        mBottom = centerY + lastBitmap.height / 2
    }
    /**-----------------------内部接口声明区--------------------------*/
    /**-----------------------内部类声明区---------------------------*/


}

interface DrawAdapter {
    fun drawLastStatyPage(canvas: Canvas)
    fun drawLastMovePage(canvas: Canvas)

    fun drawNextStatyPage(canvas: Canvas)
    fun drawNextMovePage(canvas: Canvas)
}
