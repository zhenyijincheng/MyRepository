package com.toy.qiaodata.takeredbag

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast


class RedPacketService : AccessibilityService() {

    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     * LAUCHER-微信聊天界面
     * LUCKEY_MONEY_RECEIVER-点击红包弹出的界面
     * LUCKEY_MONEY_DETAIL-红包领取后的详情界面
     */
    private val LAUCHER = "com.tencent.mm.ui.LauncherUI"
    private val LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI"
    private val LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI"

    private var isOpenRP: Boolean = false
    private var isOpenDetail = false

    /**
     * 用于判断是否屏幕是亮着的
     */
    private var isScreenOn: Boolean = false

    /**
     * 获取PowerManager.WakeLock对象
     */
    private var wakeLock: PowerManager.WakeLock? = null

    /**
     * KeyguardManager.KeyguardLock对象
     */
    private var keyguardLock: KeyguardManager.KeyguardLock? = null

    /**
     * 必须重写的方法：此方法用了接受系统发来的event。在你注册的event发生是被调用。在整个生命周期会被调用多次。
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventType = event!!.eventType
        when (eventType) {
        //通知栏来信息，判断是否含有微信红包字样，是的话跳转
            TYPE_NOTIFICATION_STATE_CHANGED -> {
                val texts = event.text
                for (text in texts) {
                    val content = text.toString()
                    if (!TextUtils.isEmpty(content)) {
                        //判断是否含有[微信红包]字样
                        if (content.contains("[微信红包]")) {
                            if (!isScreenOn()) {
                                wakeUpScreen()
                            }
                            //如果有则打开微信红包页面
                            openWeChatPage(event)

                            isOpenRP = false
                        }
                    }
                }
            }
            TYPE_WINDOW_STATE_CHANGED -> {
                val className = event.className.toString()
                //判断是否是显示‘开’的那个红包界面
                if (LUCKEY_MONEY_RECEIVER.equals(className)) {
                    val rootNode = rootInActiveWindow
                    //开始抢红包
                    openRedPacket(rootNode)
                    return
                }
                //判断是否是红包领取后的详情界面
                if (isOpenDetail && LUCKEY_MONEY_DETAIL.equals(className)) {

                    isOpenDetail = false
                    //返回桌面
                    back2Home()
                    //如果之前是锁着屏幕的则重新锁回去
                    release()
                    return
                }
                //判断是否是微信聊天界面
                if (LAUCHER.equals(className)) {
                    //获取当前聊天页面的根布局
                    val rootNode = rootInActiveWindow
                    //开始找红包
                    findRedPacket(rootNode)
                }





            }

        }
    }

    /**
     * 开启红包所在的聊天页面
     */
    private fun openWeChatPage(event: AccessibilityEvent) {
        //A instanceof B 用来判断内存中实际对象A是不是B类型，常用于强制转换前的判断
        if (event.parcelableData != null && event.parcelableData is Notification) {
            val notification = event.parcelableData as Notification
            //打开对应的聊天界面
            val pendingIntent = notification.contentIntent
            try {
                pendingIntent.send()
            } catch (e: PendingIntent.CanceledException) {
                e.printStackTrace()
            }

        }
    }


    /**
     * 释放keyguardLock和wakeLock
     */
    @SuppressLint("MissingPermission")
    fun release() {
        if (keyguardLock != null) {
            keyguardLock!!.reenableKeyguard()
            keyguardLock = null
        }
        if (wakeLock != null) {
            wakeLock!!.release()
            wakeLock = null
        }
    }


    /**
     * 遍历查找红包
     */
    private fun findRedPacket(rootNode: AccessibilityNodeInfo?) {
        if (rootNode != null) {
            //从最后一行开始找起
            for (i in rootNode.childCount - 1 downTo 0) {
                val node = rootNode.getChild(i) ?: continue
                //如果node为空则跳过该节点
                val text = node.text
                if (text != null && text.toString() == "领取红包") {
                    var parent: AccessibilityNodeInfo? = node.parent
                    //while循环,遍历"领取红包"的各个父布局，直至找到可点击的为止
                    while (parent != null) {
                        if (parent.isClickable) {
                            //模拟点击
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            //isOpenRP用于判断该红包是否点击过
                            isOpenRP = true
                            break
                        }
                        parent = parent.parent
                    }
                }
                //判断是否已经打开过那个最新的红包了，是的话就跳出for循环，不是的话继续遍历
                if (isOpenRP) {
                    break
                } else {
                    findRedPacket(node)
                }

            }
        }
    }

    /**
     * 开始打开红包
     */
    private fun openRedPacket(rootNode: AccessibilityNodeInfo) {
        for (i in 0 until rootNode.childCount) {
            val node = rootNode.getChild(i)
            if ("android.widget.Button" == node.className) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
            openRedPacket(node)
        }
    }

    /**
     * 返回桌面
     */
    private fun back2Home() {
        val home = Intent(Intent.ACTION_MAIN)
        home.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        home.addCategory(Intent.CATEGORY_HOME)
        startActivity(home)
    }

    /**
     * 判断是否处于亮屏状态
     *
     * @return true-亮屏，false-暗屏
     */
    private fun isScreenOn(): Boolean {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        isScreenOn = pm.isScreenOn
        Log.e("isScreenOn", isScreenOn.toString() + "")
        return isScreenOn
    }
    /**
     * 解锁屏幕
     */
    @SuppressLint("WakelockTimeout")
    private fun wakeUpScreen() {

        //获取电源管理器对象
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        //后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.FULL_WAKE_LOCK, "bright")

        //点亮屏幕
        if(wakeLock is PowerManager.WakeLock) {
            wakeLock!!.acquire()
        }

        //得到键盘锁管理器
        val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardLock = km.newKeyguardLock("unlock")

        //解锁
        if (keyguardLock is KeyguardManager.KeyguardLock) {
            keyguardLock!!.disableKeyguard()
        }
    }


    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    override fun onInterrupt() {
        Toast.makeText(this, "抢红包Service即将被杀死", Toast.LENGTH_SHORT).show();
    }

    override fun onServiceConnected() {
        Toast.makeText(this, "抢红包服务已启动", Toast.LENGTH_SHORT).show();
        super.onServiceConnected()
    }

    /**
     * 服务已断开
     */
    override fun onUnbind(intent: Intent): Boolean {
        Toast.makeText(this, "抢红包服务已被关闭", Toast.LENGTH_SHORT).show()
        return super.onUnbind(intent)
    }

}
