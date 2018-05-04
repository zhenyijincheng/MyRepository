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
    private lateinit var currentBitmap:Bitmap
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
        nextBitmap = BitmapFactory.decodeResource(resources, R.mipmap.share_resume_weixin)
        currentBitmap = BitmapFactory.decodeResource(resources,R.mipmap.share_resume_qq)

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
                val temple = currentBitmap
                currentBitmap = nextBitmap
                nextBitmap = temple
                lastBitmap = temple

                slideStatus = SlideStatus.Stay
                animatorStatus = AnimatorStatus.Stay
                lastX = 0f

                invalidate()

            }
        })

        prePageValueAnimator = ObjectAnimator.ofFloat(1f,0f)
        prePageValueAnimator.setDuration(1000)
        prePageValueAnimator.addListener(object :Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?){
                val temple = currentBitmap
                currentBitmap = lastBitmap
                nextBitmap = temple
                lastBitmap = temple

                slideStatus = SlideStatus.Stay
                animatorStatus = AnimatorStatus.Stay
                lastX = 0f

                invalidate()

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
            override fun drawCurrentPage(canvas: Canvas,bitmap: Bitmap) {
                canvas.save()
                canvas.drawBitmap(bitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawLastStatyPage(canvas: Canvas,bitmap: Bitmap) {
                canvas.save()
                canvas.clipRect(0f,0f,centerX,height.toFloat())
                canvas.drawBitmap(bitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawLastMovePage(canvas: Canvas,bitmap: Bitmap) {
                canvas.save()

                camera.save()
                camera.rotateY(degree)
                canvas.translate(centerX,centerY)
                camera.applyToCanvas(canvas)
                canvas.translate(-centerX,-centerY)
                camera.restore()

                canvas.clipRect(centerX,0f,width.toFloat(),height.toFloat())
                canvas.drawBitmap(bitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawNextStatyPage(canvas: Canvas,bitmap: Bitmap) {
                canvas.save()
                canvas.clipRect(centerX,0f,width.toFloat(),height.toFloat())
                canvas.drawBitmap(bitmap,mLeft,mTop,paint)
                canvas.restore()
            }
            override fun drawNextMovePage(canvas: Canvas,bitmap: Bitmap) {
                canvas.save()

                camera.save()
                camera.rotateY(degree + 180)
                canvas.translate(centerX,centerY)
                camera.applyToCanvas(canvas)
                canvas.translate(-centerX,-centerY)
                camera.restore()

                canvas.clipRect(0f,0f,centerX,height.toFloat())
                canvas.drawBitmap(bitmap,mLeft,mTop,paint)
                canvas.restore()
            }
        }
    }


    /**-----------------------生命周期回调方法区（除onCreate()方法外）--*/
    /**-----------------------事件响应方法区--------------------------*/
    /**-----------------------重载方法区--------------------------*/
    override fun onDraw(canvas: Canvas) {
        getLayout()
        if (animatorStatus == AnimatorStatus.Stay) {
            drawAdapter.drawCurrentPage(canvas,lastBitmap)
        } else {
            when(animatorStatus) {
                AnimatorStatus.PrePlaying -> {
                    drawAdapter.drawLastStatyPage(canvas,currentBitmap)
                    drawAdapter.drawNextStatyPage(canvas,lastBitmap)
                    if (degree <= 90) {
                        drawAdapter.drawLastMovePage(canvas,currentBitmap)
                    }
                    if (degree in 90.0..180.0) {
                        drawAdapter.drawNextMovePage(canvas,lastBitmap)
                    }
                }
                AnimatorStatus.NextPlaying -> {
                    drawAdapter.drawLastStatyPage(canvas,nextBitmap)
                    drawAdapter.drawNextStatyPage(canvas,currentBitmap)
                    if (degree <= 90) {
                        drawAdapter.drawLastMovePage(canvas,nextBitmap)
                    }
                    if (degree in 90.0..180.0) {
                        drawAdapter.drawNextMovePage(canvas,currentBitmap)
                    }
                }
            }
        }
    }


    private var lastX: Float = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == ACTION_DOWN) {
            lastX = event.x
        }
        if (event.action == ACTION_MOVE) {

            //1.是否开始翻页
                //1.1没有开始翻页，向左滑还是向右滑
                    //1.1.1向左滑，启动翻动到下一页动画
                    //1.1.2向右滑，启动翻动到上一页动画
                //1.2开始翻页，是上一页动画还是下一页动画
                    //1.2.1上一页动画 向左滑还是向右滑
                        //1.2.1.1 向左滑，上一页动画减进度，动画进度是否为0或者为1
                            //1.2.1.1.1 动画进度是为0或者为1，翻页状态置为否
                            //1.2.1.1.2 动画进度不为0或者1，不做操作
                        //1.2.1.2 向右滑，上一页动画增加进度，动画进度是否为0或者为1
                            //1.2.1.2.1 动画进度是为0或者为1，翻页状态置为否
                            //1.2.1.2.2 动画进度不为0或者1，不做操作
                    //1.2.2 下一页动画，向左滑还是向右滑
                        //1.2.2.1 向左滑，下一页动画减进度，动画进度是否为0或者为1
                            //1.2.2.1.1 动画进度是为0或者为1，翻页状态置为否
                            //1.2.2.1.2 动画进度不为0或者1，不做操作
                        //1.2.2.2 向右滑，下一页动画增加进度，动画进度是否为0或者为1
                            //1.2.2.2.1 动画进度是为0或者为1，翻页状态置为否
                            //1.2.2.2.2 动画进度不为0或者1，不做操作

            if (isSliding()) {
                when(moveDirection(event.x)) {
                    SlideDirection.Left -> {
                        animatorStatus = AnimatorStatus.NextPlaying
                        nextPageValueAnimator.currentPlayTime = nextPageValueAnimator.currentPlayTime + 10
                    }
                    SlideDirection.Right -> {
                        animatorStatus = AnimatorStatus.PrePlaying
                        prePageValueAnimator.currentPlayTime = prePageValueAnimator.currentPlayTime + 10
                    }
                }
            } else {
                slideStatus = SlideStatus.Moving
                when(animatorStatus) {
                    AnimatorStatus.PrePlaying -> {
                        when(moveDirection(event.x)) {
                            SlideDirection.Left -> {
                                prePageValueAnimator.currentPlayTime = prePageValueAnimator.currentPlayTime - 10
                            }
                            SlideDirection.Right -> {
                                prePageValueAnimator.currentPlayTime = prePageValueAnimator.currentPlayTime + 10
                            }
                        }
                        if (prePageValueAnimator.currentPlayTime == 1000L || prePageValueAnimator.currentPlayTime == 0L || !prePageValueAnimator.isRunning) {
                            slideStatus = SlideStatus.Stay
                            animatorStatus = AnimatorStatus.Stay
                            lastX = 0f
                        }
                    }

                    AnimatorStatus.NextPlaying -> {
                        when(moveDirection(event.x)) {
                            SlideDirection.Left -> {
                                nextPageValueAnimator.currentPlayTime = nextPageValueAnimator.currentPlayTime + 10
                            }
                            SlideDirection.Right -> {
                                nextPageValueAnimator.currentPlayTime = nextPageValueAnimator.currentPlayTime - 10
                            }
                        }
                        if (nextPageValueAnimator.currentPlayTime == 1000L || nextPageValueAnimator.currentPlayTime == 0L ||!nextPageValueAnimator.isRunning) {
                            slideStatus = SlideStatus.Stay
                            animatorStatus = AnimatorStatus.Stay
                            lastX = 0f
                        }
                    }

                }
            }
            lastX = event.x

        }
        if(event.action == ACTION_UP) {
            when(animatorStatus) {
                AnimatorStatus.PrePlaying -> prePageValueAnimator.start()
                AnimatorStatus.NextPlaying -> nextPageValueAnimator.start()
            }
        }
        return true
    }

    private fun moveDirection(movedX:Float): SlideDirection {
        val value = lastX - movedX
        return when {
            value > 0 -> SlideDirection.Left
            value < 0 -> SlideDirection.Right
            else -> SlideDirection.Stay
        }
    }

    private fun isSliding(): Boolean {
        return slideStatus != SlideStatus.Stay
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
    var slideStatus:SlideStatus = SlideStatus.Stay
    enum class SlideStatus {
        Moving,Stay
    }
    enum class SlideDirection {
        Left,Right,Stay
    }
    var animatorStatus:AnimatorStatus = AnimatorStatus.Stay
    enum class AnimatorStatus {
        NextPlaying,PrePlaying,Stay
    }


}

interface DrawAdapter {
    fun drawLastMovePage(canvas: Canvas, bitmap: Bitmap)
    fun drawLastStatyPage(canvas: Canvas, bitmap: Bitmap)

    fun drawNextStatyPage(canvas: Canvas, bitmap: Bitmap)
    fun drawNextMovePage(canvas: Canvas, bitmap: Bitmap)

    fun drawCurrentPage(canvas: Canvas, bitmap: Bitmap)
}
