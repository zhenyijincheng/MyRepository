package com.zhaopin.qiaodata.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zhaopin.qiaodata.kotlin.view.DragCityActivity
import com.zhaopin.qiaodata.kotlin.view.HentaiJumStepsActivity
import com.zhaopin.qiaodata.kotlin.view.PageViewActivity
import com.zhaopin.qiaodata.kotlin.view.ScrollConlictActivity
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_hello_world.setText("你好世界")
        btn_to_hentai_jump.setOnClickListener(this)
        btn_to_drag_city.setOnClickListener(this)
        btn_to_scroll_conflict.setOnClickListener(this)
        btn_to_page_view.setOnClickListener(this)
        Observable
//                .interval(10,TimeUnit.MILLISECONDS)
                .range(0,10)
                .onBackpressureBuffer(100)
                .observeOn(Schedulers.newThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onError(e: Throwable?) {
                        Log.d("Backpressure",e.toString())
                    }

                    override fun onCompleted() {

                    }
                    override fun onNext(t: Int?) {
                        Log.d("Backpressure", t.toString())
                        try {
                            Thread.sleep(100)
                        } catch (e :Exception) {

                        }
                        request(1)
                    }
                    override fun onStart() {
                        request(1)
                    }

                })
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_to_hentai_jump -> startActivity(Intent(this,HentaiJumStepsActivity::class.java))
            R.id.btn_to_drag_city -> startActivity(Intent(this,DragCityActivity::class.java))
            R.id.btn_to_scroll_conflict -> startActivity(Intent(this,ScrollConlictActivity::class.java))
            R.id.btn_to_page_view->startActivity(Intent(this,PageViewActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        var a  = 10
        var b :Long = a.toLong()
        Toast.makeText(baseContext,"",Toast.LENGTH_SHORT).show()
    }
}
