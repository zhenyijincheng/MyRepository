package com.toy.qiaodata.imitatexiaomisportmodel.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.toy.qiaodata.imitatexiaomisportmodel.R
import java.util.*



/**
 *
 * Created by YANG on 2018/4/23.
 */

class MyView: RelativeLayout {


    /**-----------------------静态Activity启动方法区-------------------*/
    /**-----------------------常量声明区------------------------------*/
    /**-----------------------UI控件成员变量声明区---------------------*/
    /**-----------------------普通成员变量声明区-----------------------*/
    var valueAnimatorValue:Float = 0f
    lateinit var sweepGradient:SweepGradient

    private val circlePaint = Paint()
    private val particlePaint = Paint()

    private val circleCenterPoint :Point = Point(0.0, 0.0)
    private val ccp1 :Point = Point(0.0, 0.0)
    private val ccp2 :Point = Point(0.0, 0.0)
    private val ccp3 :Point = Point(0.0, 0.0)
    private val ccp4 :Point = Point(0.0, 0.0)
    private val ccp5 :Point = Point(0.0, 0.0)

    private var radius:Float = 400f
    var circleCenterX = 600.0
    var circleCenterY = 600.0
    private val randomParamRange = 20
    private val randomParamAdjust = 10
    val random = Random(System.currentTimeMillis())

    private val particleLinkedList:LinkedList<Particle> = LinkedList()
    private val LINK_LIST_MAX_LENGTH = 100
    /**-----------------------初始化相关方法区------------------------*/
    constructor(context: Context?) :this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr) {
        init()
    }



    private fun init() {
        setWillNotDraw(false)

        val va = ValueAnimator.ofFloat(0f,1f)
        va.repeatCount = 100
        va.repeatMode = ValueAnimator.RESTART
        va.duration = 5000
        va.interpolator = LinearInterpolator()
        va.addUpdateListener {
            valueAnimatorValue = it.animatedValue as Float
//            valueAnimatorValue = 1f
            Log.d("MyView", "ValueAnimator: $valueAnimatorValue")
            invalidate()
        }


        initPaint()

        va.start()

    }




    /**-----------------------生命周期回调方法区（除onCreate()方法外）--*/
    /**-----------------------事件响应方法区--------------------------*/
    /**-----------------------普通逻辑方法区--------------------------*/

    private fun initPaint() {
        if (width == 0 || height == 0) {
            circleCenterX = 600.0
            circleCenterY = 600.0
        } else {
            circleCenterX = (width / 2).toDouble()
            circleCenterY = (height / 2).toDouble()
        }

        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 2f

        particlePaint.style = Paint.Style.FILL_AND_STROKE
        particlePaint.isAntiAlias = true
        particlePaint.color = resources.getColor(R.color.white)


        sweepGradient = SweepGradient(circleCenterX.toFloat(), circleCenterY.toFloat(), Color.parseColor("#00FFFFFF"),
                Color.parseColor("#FFFFFF"))

        setCirclePoint(circleCenterPoint, circleCenterX, circleCenterY)
        circlePaint.shader = sweepGradient


        setCirclePoint(ccp1, getRandom(circleCenterPoint.x), getRandom(circleCenterPoint.y))
        setCirclePoint(ccp2, getRandom(circleCenterPoint.x), getRandom(circleCenterPoint.y))
        setCirclePoint(ccp3, getRandom(circleCenterPoint.x), getRandom(circleCenterPoint.y))
        setCirclePoint(ccp4, getRandom(circleCenterPoint.x), getRandom(circleCenterPoint.y))
        setCirclePoint(ccp5, getRandom(circleCenterPoint.x), getRandom(circleCenterPoint.y))
    }



    private fun setCirclePoint(ccp: Point,x:Double,y:Double) {
        ccp.x = x
        ccp.y = y
    }

    private fun getRandom(p: Double):Double {
        return (p + random.nextFloat() * randomParamRange + randomParamAdjust)
    }

    private fun getCurrentEmitPoint():Point {
        val fl = (random.nextFloat() * 10 / 2).toInt()
        var ccp:Point
        when(fl) {
            0->ccp = ccp1
            1->ccp = ccp2
            2->ccp = ccp3
            3->ccp = ccp4
            4->ccp = ccp5
            else-> ccp = circleCenterPoint
        }
        return Point((Math.cos(getAngleInRadius(valueAnimatorValue)) * radius + ccp.x), (Math.sin(getAngleInRadius(valueAnimatorValue)) * radius + ccp.y))
    }

    private fun getAngleInRadius(floatScale: Float):Double {
        return floatScale / 1f * 2 * Math.PI
    }

    private fun drawCircle(canvas: Canvas, ccp: Point) {
        canvas.save()
        canvas.rotate(valueAnimatorValue / 1f * 360, ccp.x.toFloat(), ccp.y.toFloat())
        canvas.drawCircle(ccp.x.toFloat(), ccp.y.toFloat(), radius, circlePaint)
        canvas.restore()
    }

    private fun emitParticle(canvas: Canvas) {


        if (particleLinkedList.size < LINK_LIST_MAX_LENGTH) {
            val currentEmitPoint = getCurrentEmitPoint()
            particleLinkedList.addFirst(Particle.Factory(currentEmitPoint,getParticleEmiteDirection(currentEmitPoint)))
        } else {
            val last = particleLinkedList.last
            particleLinkedList.removeLast()
            val currentEmitPoint = getCurrentEmitPoint()
            Particle.Factory(last, currentEmitPoint,getParticleEmiteDirection(currentEmitPoint))
            particleLinkedList.addFirst(last)

        }
        for (i in 0 until  particleLinkedList.size - 1) {
            drawParticle(canvas,particleLinkedList.get(i).getNext(),i)
        }

    }

    private fun drawParticle(canvas: Canvas, next: Point, i: Int) {
        canvas.save()
        particlePaint.alpha = (255 * (LINK_LIST_MAX_LENGTH - i) / LINK_LIST_MAX_LENGTH)
        canvas.drawCircle(next.x.toFloat(), next.y.toFloat(),6f,particlePaint)
        canvas.restore()
    }

    private fun getParticleEmiteDirection(currentPoint: Point):Double {
        //根据currentEmitPoint,获得切线方向
        val d = random.nextDouble() * Math.PI - Math.PI / 2
        var sinh:Double
        try {
            val toDouble = ((currentPoint.y - circleCenterPoint.y) / (currentPoint.x - circleCenterPoint.x)).toDouble()
            sinh = Math.sinh(toDouble)

        } catch (e:ArithmeticException) {
            sinh = Math.PI / 4
        }

        val d1 = sinh - Math.PI / 2 + d
        Log.d("SIN"," " + d1)
        return d1

    }

    /**-----------------------重载的逻辑方法区------------------------*/
    override fun onDraw(canvas: Canvas)  {
        super.onDraw(canvas)

        if (circleCenterX != (width /2).toDouble()) {
            initPaint()
        }

        drawCircle(canvas,ccp1)
        drawCircle(canvas,ccp2)
        drawCircle(canvas,ccp3)
        drawCircle(canvas,ccp4)
        drawCircle(canvas,ccp5)

        emitParticle(canvas)


    }




    /**-----------------------内部接口声明区--------------------------*/
    /**-----------------------内部类声明区---------------------------*/
    class Particle {
        companion object Factory {
            val emitDistance = 1f


            public  fun Factory(currentPoint: Point,direction:Double):Particle {
                return Particle(currentPoint,direction)
            }

            public fun Factory(tagParticle:Particle,currentPoint: Point,direction:Double):Particle {
                tagParticle.currentPoint = currentPoint
                tagParticle.direction = direction
                tagParticle.speed = 0f
                tagParticle.nextPoint = point(direction,currentPoint,tagParticle.speed)
                return tagParticle
            }

            private fun point(direction: Double, currentPoint: Point,speed:Float) :Point {

                val cos = Math.cos(direction)
                val sin = Math.sin(direction)
                return Point((cos * emitDistance * speed + currentPoint.x), (sin * emitDistance  * speed + currentPoint.y))

            }

        }

        private constructor(currentPoint: Point,direction:Double) {
            this.currentPoint = currentPoint
            this.direction = direction
            speed = 0f
            nextPoint = point(direction, currentPoint,speed)

        }

        var nextPoint:Point
        var currentPoint:Point
        var direction: Double
        var speed:Float = 0f





        public fun getNext():Point {
            currentPoint = nextPoint
            val direction1 = direction
            Log.d("NEXT","X:" + (Math.cos(direction1)* emitDistance).toInt() + " Y:" + (Math.sin(direction1)* emitDistance).toInt())
            nextPoint = point(direction1, currentPoint,speed)
            speed += 0.1f
            return nextPoint
        }


    }

    class Point(public var x: Double, public var y: Double) {
    }
}
