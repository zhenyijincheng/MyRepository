package com.zhaopin.qiaodata.kotlin.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import com.zhaopin.qiaodata.kotlin.R

import kotlinx.android.synthetic.main.activity_fibonacci.*
import java.math.BigDecimal
import java.math.BigInteger

class FibonacciActivity : AppCompatActivity() {

    val sqrt5 = Math.sqrt(5.0)

    val const1 = BigDecimal.valueOf(sqrt5 / 5)

    val const2 = BigDecimal.valueOf((1 + sqrt5) / 2)

    val const3 = BigDecimal.valueOf((1 - sqrt5) / 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonacci)

        btn_submit.setOnClickListener {
            if (!TextUtils.isEmpty(edit_number.text)) {
                tv_result.text = fibonacci(edit_number.text.toString().toInt())
            }
        }
    }

    fun  fibonacci(n:Int):String{
        val starTime = System.currentTimeMillis()
        val result = const1 * (const2.pow(n) - const3.pow(n))
        Log.d("time","耗时:" + (System.currentTimeMillis() - starTime) + "mS")
        return result.toBigInteger().toString()
    }


}

