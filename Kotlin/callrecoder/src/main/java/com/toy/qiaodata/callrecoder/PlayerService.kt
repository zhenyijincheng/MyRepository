package com.toy.qiaodata.callrecoder

import android.accessibilityservice.AccessibilityService
import android.content.*
import android.content.ContentValues.TAG
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat


class PlayerService : AccessibilityService() {

    /**-----------------------常量声明区------------------------------*/
    private val TAG = "RecorderService"
    private val TAG1 = "手机通话状态"
    /**-----------------------UI控件成员变量声明区---------------------*/
    /**-----------------------普通成员变量声明区-----------------------*/
    //音频录制
    private var recoder: MediaRecorder? = null

//    private var dataFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var dataFormat:SimpleDateFormat = (SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat).apply {
        applyPattern("yyyy-MM-dd HH:mm:ss")
    }
    /**
     * 监听拨号广播，以便获取用户拨出的电话号码
     */
    private var phoneCallReceiver: PhoneCallReceiver? = null
    private var intentFilter: IntentFilter? = null
    /**
     * 网络状态改变广播，当网络畅通的状态下，把用户未上传的录音文件都上传掉
     */
    private var networkConnectChangedReceiver:NetworkConnectChangedReceiver? = null
    private var intentFilter2:IntentFilter? = null

    /**
     * 当前通话对象的电话号码
     */
    private var currentCallNum = "";
    /**
     * 区分来电和去电
     */
    private var previousStats = 0;
    /**
     * 当前正在录制的文件
     */
    private var currentFile = "";
    /**
     * 保存未上传的录音文件
     */
    private  var unUploadFile: SharedPreferences? = null
    private var dirPath = ""
    private var isRecording = false
    /**-----------------------内部接口声明区--------------------------*/
    /**-----------------------初始化相关方法区------------------------*/
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
        var  tm:TelephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager;
        // 监听电话状态
//        tm.listen(MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
        phoneCallReceiver = PhoneCallReceiver()
        intentFilter = IntentFilter()
        //设置拨号广播过滤
        intentFilter!!.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(phoneCallReceiver, intentFilter);
        //注册拨号广播接收器
        networkConnectChangedReceiver = NetworkConnectChangedReceiver();
        intentFilter2 = IntentFilter()
        //设置网络状态改变广播过滤
        intentFilter2!!.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter2!!.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter2!!.addAction("android.net.wifi.STATE_CHANGE");
        //注册网络状态改变广播接收器
        registerReceiver(networkConnectChangedReceiver, intentFilter2);
        unUploadFile = getSharedPreferences("un_upload_file", 0);
        unUploadFile!!.edit().putString("description", "未上传的录音文件存放路径").apply()
        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.ct.phonerecorder/";
    }
    /**-----------------------事件响应方法区--------------------------*/
    /**-----------------------普通逻辑方法区--------------------------*/
    /**
     * 上传因为网络或者其他原因，暂未上传或者上传失败的文件，重新上传
     */
    fun uploadUnUploadedFiles() {
        //获取当前还未上传的文件，并把这些文件上传
        val files:String = unUploadFile!!.getString("unUploadFile", "")
        unUploadFile!!.edit().putString("unUploadFile", "").apply()
        if (files.equals("")) {
            return
        }
        val fileArry = files.split(";")
        var len = fileArry.size
        for (file in fileArry) {
            upload(file)
        }
    }

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     */
    fun upload(file:String) {
        val file1 = File(file)
        if ( !file1.exists()) {
            //文件不存在
            return
        }
//        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {//如果没有网络
        if (false) {
            saveUnUploadFIles(file)//保存到本地
            return;
        }
        val map = HashMap<String, String>();
        map.put("type", "1");
        var url = "http://192.168.1.158:8082/uploader";

        //TODO 上传文件的代码
    }

    /**
     * 保存未上传的录音文件
     *
     * @param file 未上传的录音文件路径
     */
    fun saveUnUploadFIles(file:String) {
        var files = unUploadFile!!.getString("unUploadFile", "");
        if (files.equals("")) {
            files = file
        } else {
            val sb =  StringBuilder(files);
            files = sb.append(";").append(file).toString();
        }
        unUploadFile!!.edit().putString("unUploadFile", files).apply()
    }

    /**-----------------------重载的逻辑方法区------------------------*/
    /**-----------------------生命周期回调方法区（除onCreate()方法外）--*/
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected");
        Toast.makeText(getApplicationContext(), "自动录音服务已启动", Toast.LENGTH_LONG).show();
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(TAG,"onAccessibilityEvent")
        Log.d(TAG, "eventType " + event!!.eventType)
    }
    override fun onInterrupt() {
        Log.d(TAG,"onInterrupt")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        Log.d(TAG,"onDestroy")
        super.onDestroy()
    }
    /**-----------------------内部类声明区---------------------------*/
    class NetworkConnectChangedReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            /**
             * 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
             * 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
             * 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
             */
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent!!.getAction())) {
                var manager: ConnectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
                Log.i(TAG, "CONNECTIVITY_ACTION");

                var activeNetwork: NetworkInfo = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected()) {
                        //当前网络可用
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            // connected to wifi
                            Log.e(TAG, "当前WiFi连接可用 ");
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            // connected to the mobile provider's data plan
                            Log.e(TAG, "当前移动网络连接可用 ");
                        }
//                        uploadUnUploadedFiles()
                    } else {
                        Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                    }

                } else {   // not connected to the internet
                    Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                }


            }
        }

    }


}
