package com.zhaopin.qiaodata.kotlin.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.zhaopin.qiaodata.kotlin.R
import kotlinx.android.synthetic.main.activity_hentai_jum_steps.*

/**
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
 */
class HentaiJumStepsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hentai_jum_steps)

        btn_start_jum.setOnClickListener(View.OnClickListener {
            val num = et_num?.text?.toString()
            val toInt = num?.toInt() ?: 0
            tv_result?.setText("结果为:${jumpFloorII(toInt)}")
        })
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    /**
     * 递归实现
     */
    fun jumpFloorII(target: Int): Int {
        when (target) {
            0 -> return 0
            1 -> return 1
            else -> {
                var steps = 1
                for (i in 1..target) {
                    steps += jumpFloorII(target - i)
                }
                return steps
            }
        }
    }

    /**
     * 循环方法实现
     */
    fun jumpFloorIII(target: Int): Int {
        return Math.pow(2.0,(target - 1).toDouble()).toInt()
    }
}
