package com.toy.qiaodata.redbubble.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.ViewGroup
import android.widget.FrameLayout
import com.toy.qiaodata.geometrylibrary.`object`.Circle
import com.toy.qiaodata.geometrylibrary.util.GeometryUtils
import com.toy.qiaodata.geometrylibrary.`object`.Line
import com.toy.qiaodata.geometrylibrary.`object`.Point

/**
 * create by YANG on 2018/7/11 14:17
 */
class BubbleParent : FrameLayout {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private val viewList: ArrayList<BubbleView>

    private val paint: Paint = Paint()
    private val paint1: Paint = Paint()


    private var touchedX: Float = (width / 2).toFloat()
    private var touchedY: Float = (height / 2).toFloat()
    private var isBeginTouch = false

    private var originalX: Float = 0.0f
    private var originalY: Float = 0.0f
    private var radius: Float = 0f

    private  var childOnChangeListener: BubbleView.OnChangeListener? = null


    init {

        paint.color = Color.parseColor("#FF0000")
        paint1.color = Color.parseColor("#FF0000")
        paint1.style = Paint.Style.FILL

        viewList = ArrayList()
        setViewList(this)

    }


    private fun setViewList(viewGroup: ViewGroup) {

        for (i in 0..viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is BubbleView) {
                viewList.add(child)
            } else if (child is ViewGroup) {
                setViewList(child)
            }
            continue
        }
    }



    fun setBeginTouch(originalX: Float, originalY: Float, radius: Float, onChangeListener: BubbleView.OnChangeListener) {
        isBeginTouch = true
        this.radius = radius
        this.originalX = originalX
        this.originalY = originalY
        this.childOnChangeListener = onChangeListener;
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var line: Line? = null

    private  var verticalLine1: Line? = null

    private  var circle1: Circle? = null

    private var crossoverPoint1: Array<out Point>? = null

    private var verticalLine2: Line? = null

    private  var circle2: Circle? = null

    private var crossoverPoint2: Array<out Point>? = null

    private var centerPoint: Point? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        touchedX = event.x
        touchedY = event.y

        if(isBeginTouch) {
            line = Line(touchedX.toDouble(), touchedY.toDouble(), originalX.toDouble(), originalY.toDouble())

            centerPoint = GeometryUtils.getTwoPointCenterPoint(touchedX.toDouble(), touchedY.toDouble(), originalX.toDouble(), originalY.toDouble())
            val distance = GeometryUtils.getTwoPointDistance(touchedX.toDouble(), touchedY.toDouble(), originalX.toDouble(), originalY.toDouble())
            childOnChangeListener!!.onRadiusChange(((50 - distance) / 50).toFloat() * radius)

            verticalLine1 = line!!.getVerticalLine(touchedX.toDouble(), touchedY.toDouble())
            circle1 = Circle(radius, touchedX, touchedY)
            crossoverPoint1 = GeometryUtils.getCrossoverPoint(verticalLine1, circle1)


            verticalLine2 = line!!.getVerticalLine(originalX.toDouble(), originalY.toDouble())
//            circle2 = Circle(radius, originalX, originalY)
            circle2 = Circle(((50 - distance) / 50).toFloat() * radius, originalX, originalY)
            crossoverPoint2 = GeometryUtils.getCrossoverPoint(verticalLine2, circle2)

            invalidate()
        }
        when (event.action) {
            ACTION_UP -> {
                isBeginTouch = false
                performClick()
            }
        }
        return isBeginTouch
    }

    private val path: Path = Path()

    override fun onDraw(canvas: Canvas) {
        if (isBeginTouch) {
            canvas.drawCircle(touchedX, touchedY, 25f, paint)
            path.reset()
            path.moveTo(crossoverPoint1!![0].x.toFloat(), crossoverPoint1!![0].y.toFloat())

            path.lineTo(crossoverPoint1!![1].x.toFloat(), crossoverPoint1!![1].y.toFloat())

//            path.lineTo(crossoverPoint2!![1].x.toFloat(), crossoverPoint2!![1].y.toFloat())
            path.quadTo(centerPoint!!.x.toFloat(), centerPoint!!.y.toFloat(),crossoverPoint2!![1].x.toFloat(), crossoverPoint2!![1].y.toFloat())

            path.lineTo(crossoverPoint2!![0].x.toFloat(), crossoverPoint2!![0].y.toFloat())

//            path.lineTo(crossoverPoint1!![0].x.toFloat(), crossoverPoint1!![0].y.toFloat())
            path.quadTo(centerPoint!!.x.toFloat(), centerPoint!!.y.toFloat(),crossoverPoint1!![0].x.toFloat(), crossoverPoint1!![0].y.toFloat())

            path.fillType = Path.FillType.WINDING
            canvas.drawPath(path, paint1)
        }


    }

}