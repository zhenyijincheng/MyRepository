package com.zhaopin.qiaodata.kotlin.view

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.zhaopin.qiaodata.kotlin.R
import kotlinx.android.synthetic.main.activity_scroll_conlict.*

class ScrollConlictActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_conlict)
        val scrollConlictActivity = this
        viewPager.adapter = object : PagerAdapter() {

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val inflate = View.inflate(scrollConlictActivity, R.layout.item_page, null)
//                inflate.setOnClickListener(View.OnClickListener {
//                    Log.i("TouchEvent","ViewPagerChildClick")
//                })
                inflate.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                        Log.i("TouchEvent","ViewPagerChildClick action:" + p1!!.action)
                        return true
                    }

                })
                container!!.addView(inflate)
                return inflate
            }

            override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return 10
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                container!!.removeView(`object` as View?)
            }
        }
    }
}
