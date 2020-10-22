package com.pinduo.auto.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.core.CommonAccessbility
import com.pinduo.auto.core.FloatWindowAccessbility
import com.pinduo.auto.core.LivePlayAccessibility
import com.pinduo.auto.http.entity.TaskEntity
import com.pinduo.auto.im.OnSocketListener
import com.pinduo.auto.im.SocketClient
import com.pinduo.auto.utils.LogUtils
import com.pinduo.auto.widget.timer.MyScheduledExecutor
import com.pinduo.auto.widget.timer.TimerTickListener
import com.yhao.floatwindow.FloatWindow
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MyAccessibilityService : AccessibilityService() {


    private val socketClient by lazy { SocketClient.instance }
    private val runnable by lazy { MyScheduledExecutor.INSTANCE }

    private val initialDelay:Long = 1L
    private val period:Long = 1L
    private val max:Long = Long.MAX_VALUE

    private val service by lazy {  Executors.newScheduledThreadPool(4) }
    private val uiHandler by lazy { MyApplication.instance.getUiHandler() }


    override fun onCreate() {
        super.onCreate()
        CommonAccessbility.INSTANCE.initService(this)
        FloatWindowAccessbility.INSTANCE.initService(this)
        LivePlayAccessibility.INSTANCE.initService(this)

        (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).addAccessibilityStateChangeListener {
            LogUtils.logGGQ("AccessibilityManager：${it}")
            if(it){
                FloatWindow.get()?.apply {
                    if(!this.isShowing){
                        this.show()
                    }
                }
            }else{
                FloatWindow.get()?.apply {
                    if(this.isShowing) {
                        this.hide()
                    }
                }
            }
        }

        socketClient.setListener(object : OnSocketListener{
            override fun call(entity: TaskEntity) {
                LogUtils.logGGQ("收到数据：${entity.toString()}")
                LivePlayAccessibility.INSTANCE.doLive()
            }
        })
        runnable.setListener(object :TimerTickListener{
            override fun onStart(name: String, job: String) {
                LogUtils.logGGQ("开始任务：${name} --- ${job}")
                uiHandler.sendMessage("开始任务：${name} --- ${job}")
            }

            override fun onTick(tick: Long) {
                LogUtils.logGGQ("tick：${tick}")
                uiHandler.sendMessage("tick：${tick}")
            }

            override fun onMark(mark: Long) {
                LogUtils.logGGQ("onMark：${mark}")
                uiHandler.clearMessage()
                uiHandler.sendMessage("onMark：${mark}")
            }

            override fun onStop(name: String, job: String) {
                LogUtils.logGGQ("结束任务：${name} --- ${job}")
                uiHandler.sendMessage("结束任务：${name} --- ${job}")
                //uiHandler.clearMessage()
            }
        })
    }


    override fun onServiceConnected() {
        super.onServiceConnected()
        socketClient.onConnect()
        runnable.isRing().let {
            if(!it){
                service.scheduleAtFixedRate(runnable,initialDelay,period, TimeUnit.SECONDS)
                runnable.onReStart("app","task",max)
            }
        }

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {
        LogUtils.logGGQ("onInterrupt")
    }


    override fun onDestroy() {
        super.onDestroy()
        LogUtils.logGGQ("onDestroy")
    }


}