package com.toy.qiaodata.callrecoder

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.system.Os.listen
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import android.util.Log



/**
 * Created by YANG on 2018/3/8.
 */
class PhoneCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 如果是去电
        if (intent!!.action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

//            if (PlayerService.player != null && PlayerService.player.musicIsPlaying()) {
//                Log.e("loge", "去电======================================")
//                val tm = context?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
//                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
//            }

        } else {
            // 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            // 如果我们想要监听电话的拨打状况，需要这么几步 :
//            if (PlayerService.player != null && PlayerService.player.musicIsPlaying()) {
//                Log.e("loge", "来电======================================")
//
//                val tm = context?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
//                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
//            }

        }
    }

    // 设置一个监听器
    var listener: PhoneStateListener = object : PhoneStateListener() {

        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber)
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> println("挂断")
                TelephonyManager.CALL_STATE_OFFHOOK -> println("接听")
                TelephonyManager.CALL_STATE_RINGING -> println("响铃:来电号码" + incomingNumber)
            }// 输出来电号码
        }
    }
}