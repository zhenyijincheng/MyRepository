package com.zhaopin.qiaodata.kotlin.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.zhaopin.qiaodata.kotlin.R

/**
 * Created by YANG on 2017/12/19.
 */

class BackgroundView:FrameLayout {
    /**-----------------------常量声明区------------------------------*/
    /**-----------------------UI控件成员变量声明区---------------------*/
    val ivBackground1: ImageView?
    val ivBackground2:ImageView?
    /**-----------------------普通成员变量声明区-----------------------*/
    private var BG_MARGIN = -200f //背景图片最高点高出屏幕的绝对值
    private var DRAG_TOP_0 = 0//DragView输入的top最高点的值
    private var DRAG_RANGE: Int = 0  //DragView拖动的范围
    private var BG_RANGE:Int = 0 //背景拖动的距离
    private var bp:Bitmap? = null
    /**-----------------------内部接口声明区--------------------------*/
    /**-----------------------初始化相关方法区------------------------*/
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,-1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, BG_MARGIN: Int,DRAG_TOP_0:Int ,DRAG_RANGE: Int, BG_RANGE: Int):this(context) {
        this.BG_MARGIN = BG_MARGIN.toFloat()
        this.DRAG_TOP_0 = DRAG_TOP_0
        this.DRAG_RANGE = DRAG_RANGE
        this.BG_RANGE = BG_RANGE
    }

    init {

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.city,options)
        bp = getZoomImage(bitmap,width.toDouble(),1200.toDouble())
        ivBackground1 = ImageView(context)
        ivBackground1.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT,1200)
        ivBackground1.scaleType = ImageView.ScaleType.FIT_XY
        ivBackground1.setBackgroundDrawable(BitmapDrawable(bp))
        ivBackground2 = ImageView(context)
        ivBackground2.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, 1200)
        ivBackground2.scaleType = ImageView.ScaleType.FIT_XY

        val inflate = View.inflate(context, R.layout.layout_covered_content, null)
        addView(ivBackground1)
        addView(inflate)
        addView(ivBackground2)


    }

    /**-----------------------事件响应方法区--------------------------*/
    fun onScroll(top: Int) {
        val bg1Top = getBg1Top(top).toInt()
        setBg1MarginTop(bg1Top)
        setBg2Top(top,bg1Top)
    }

    /**-----------------------普通逻辑方法区--------------------------*/
    private fun getBg1Top(top: Int): Float {
        val rate = (DRAG_RANGE.toFloat() / BG_RANGE.toFloat())
        return (top.toFloat() - this.DRAG_TOP_0.toFloat()) / rate + BG_MARGIN
    }
    private fun setBg2Top(top: Int, bg1Top: Int) = try {
        val i = top - bg1Top
        val bitmapDogHeight:Int = bp?.height ?: 0
        val bitmapDogWidth:Int = bp?.width ?: 0
        val bitmap = Bitmap.createBitmap(bp, 0, i, bitmapDogWidth,bitmapDogHeight - i)
        ivBackground2?.setBackgroundDrawable(BitmapDrawable(bitmap))
        val layoutParams = ivBackground2?.layoutParams as FrameLayout.LayoutParams
        layoutParams.height = bitmapDogHeight - i
        layoutParams.setMargins(0,top, 0,-top)
        ivBackground2.layoutParams = layoutParams
    } catch (e: Exception) {

    }
    private fun setBg1MarginTop(top: Int) {
        val layoutParams = ivBackground1?.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, top, 0, 0)
        ivBackground1.layoutParams = layoutParams
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    fun getZoomImage(orgBitmap: Bitmap?, newWidth: Double, newHeight: Double): Bitmap? {
        if (null == orgBitmap) {
            return null
        }
        if (orgBitmap.isRecycled) {
            return null
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null
        }

        // 获取图片的宽和高
        val width = orgBitmap.width.toFloat()
        val height = orgBitmap.height.toFloat()
        // 创建操作图片的matrix对象
        val matrix = Matrix()
        // 计算宽高缩放率
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight)
        val createBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, width.toInt(), height.toInt(), matrix, true)
        return createBitmap

    }
    /**-----------------------重载的逻辑方法区------------------------*/

    /**-----------------------内部类声明区---------------------------*/

}
