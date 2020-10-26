package com.pinduo.auto.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.andro_accessibility_api.api.waitForApp
import cn.vove7.andro_accessibility_api.api.withId
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.app.global.Constants
import com.pinduo.auto.core.CommonAccessbility
import com.pinduo.auto.core.FloatWindowAccessbility
import com.pinduo.auto.core.LivePlayAccessibility
import com.pinduo.auto.http.entity.TaskEntity
import com.pinduo.auto.im.OnSocketListener
import com.pinduo.auto.im.SocketClient
import com.pinduo.auto.utils.LogUtils
import com.pinduo.auto.widget.observers.ObserverManager
import com.pinduo.auto.widget.timer.MyScheduledExecutor
import com.pinduo.auto.widget.timer.TimerTickListener
import com.yhao.floatwindow.FloatWindow
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MyAccessibilityService : AccessibilityApi() {


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
                val software:String = entity.software
                val task:String = entity.task
                val message:String = entity.message
                if(TextUtils.isEmpty(software)){
                    uiHandler.sendMessage("message：${message}")
                }else{
                    if(!TextUtils.isEmpty(message) && TextUtils.equals("stop",message)) {
                        if(TextUtils.equals(Constants.Task.douyin,software)){
                            when(task){
                                Constants.Task.task3 ->{
                                    LivePlayAccessibility.INSTANCE.setRoomId("")
                                    LivePlayAccessibility.INSTANCE.setOnRoom(false)
                                    ObserverManager.instance.remove(Constants.Task.task3)
                                    CommonAccessbility.INSTANCE.douyin2Main()
                                    uiHandler.sendMessage("message：${message}")
                                }
                            }
                        }else if(TextUtils.equals(Constants.Task.kuaishou,software)){

                        }
                        return
                    }

                    when(task){
                        Constants.Task.task1 ->{

                        }
                        Constants.Task.task2 ->{

                        }
                        Constants.Task.task3 ->{

                            val zxTime:String = entity.zx_time  // 60秒
                            val zhiboNum:String = entity.zhibo_num

                            LivePlayAccessibility.INSTANCE.setRoomId("")
                            LivePlayAccessibility.INSTANCE.setOnRoom(false)

                            if(TextUtils.isEmpty(software) && TextUtils.isEmpty(zxTime) && TextUtils.isEmpty(zhiboNum)){
                                LogUtils.logGGQ("数据有误")
                                uiHandler.sendMessage("开始任-->>>数据有误")
                            }else{
                                socketClient.onReceiveStatus()
                                runnable.onReStart(software,task,zxTime.toLong())
                                LivePlayAccessibility.INSTANCE.doLive(software,zhiboNum)
                            }
                        }

                        Constants.Task.task4 ->{
                            uiHandler.sendMessage("收到任务：${task}")
                            val content:String = entity.fayan
                            if(LivePlayAccessibility.INSTANCE.getOnRoom()){
                                LivePlayAccessibility.INSTANCE.speak(software,content)
                            }else{
                                uiHandler.sendMessage("---不在直播间---")
                            }
                        }
                    }
                }
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
                socketClient.sendParentSuccess()
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


    override val enableListenAppScope: Boolean
        get() = true





    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName:String? = event?.packageName.toString()
        if(!TextUtils.isEmpty(packageName)){
            if(TextUtils.equals(Constants.GlobalValue.douyinPackage,packageName)){
                val className:String? = event?.className.toString()
                className?.let {
                    when(it){
                        Constants.Douyin.PAGE_MAIN ->{
                            ObserverManager.instance.notifyObserver(Constants.Task.task3,it)
                        }

                        Constants.Douyin.PAGE_LIVE_ROOM ->{
                            ObserverManager.instance.notifyObserver(Constants.Task.task3,it)
                        }
                    }
                }
            }else if(TextUtils.equals(Constants.GlobalValue.kuaishouPackage,packageName)){
                val className:String? = event?.className.toString()
                className?.let {
                    when(it){

                    }
                }

            }
        }
    }

    override fun onInterrupt() {
        LogUtils.logGGQ("onInterrupt")
    }


    override fun onDestroy() {
        super.onDestroy()
        LogUtils.logGGQ("onDestroy")
    }


}