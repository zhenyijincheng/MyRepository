package com.toy.qiaodata.redbubble

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.toy.qiaodata.geometrylibrary.`object`.Circle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circle = Circle(10, 3, 4)

        val x = circle.getX(14.0)
        if (x != null) {
            Log.d("activity", "x1:${x[0]} + x2:${x[1]}")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("activity", "x:${event.x} + y:${event.y}")
        return super.onTouchEvent(event)
    }
}
