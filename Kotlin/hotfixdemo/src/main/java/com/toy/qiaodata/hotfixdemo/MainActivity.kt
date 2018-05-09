package com.toy.qiaodata.hotfixdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Beta.betaPatchListener = object : BetaPatchListener {
            override fun onPatchReceived(s: String) {
                Toast.makeText(applicationContext,"收到补丁" + s,Toast.LENGTH_SHORT).show()
            }

            override fun onDownloadReceived(savedLength: Long, totalLength: Long) {
                Toast.makeText(application, String.format(Locale.getDefault(), "%s %d%%",
                        Beta.strNotificationDownloading,
                        (if (totalLength == 0L) 0 else savedLength * 100 / totalLength).toInt()),
                        Toast.LENGTH_SHORT).show()
            }

            override fun onDownloadSuccess(s: String) {
                Toast.makeText(application, "补丁下载成功", Toast.LENGTH_SHORT).show()
            }

            override fun onDownloadFailure(s: String) {
                Toast.makeText(application, "补丁下载失败", Toast.LENGTH_SHORT).show()

            }

            override fun onApplySuccess(msg: String) {
                Toast.makeText(application, "补丁应用成功", Toast.LENGTH_SHORT).show()
            }

            override fun onApplyFailure(msg: String) {
                Toast.makeText(application, "补丁应用失败", Toast.LENGTH_SHORT).show()
            }

            override fun onPatchRollback() {
                Toast.makeText(application, "补丁回滚", Toast.LENGTH_SHORT).show()
            }
        }
        tv_hello.text = "this is can notify user version ()()()"

        btn_check_update.setOnClickListener {
            Beta.checkUpgrade()
        }

    }
}
