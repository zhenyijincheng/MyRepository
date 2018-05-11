package com.toy.qiaodata.runshellcommend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (RootCmd.haveRoot()) {
            Log.i("RootCmdResult:",RootCmd.execRootCmd("chmod 777 /data"))
            Log.i("RootCmdResult:",RootCmd.execRootCmd("chmod 777 /data/data"))
        }
    }
}
