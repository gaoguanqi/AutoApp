package com.pinduo.auto.core

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.app.global.Constants
import com.pinduo.auto.utils.LogUtils
import com.pinduo.auto.utils.TaskUtils
import com.pinduo.auto.utils.WaitUtil
import com.pinduo.auto.widget.observers.ObserverListener
import com.pinduo.auto.widget.observers.ObserverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class LivePlayAccessibility private constructor():BaseAccessbility(), ObserverListener {


    private var roomId:String = ""

    private var isRoom:Boolean = false



    companion object {
        val INSTANCE:LivePlayAccessibility by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            LivePlayAccessibility()
        }
    }

    fun getRoomId():String {
        return roomId
    }

    fun setRoomId(id:String){
        this.roomId = id
    }

    fun getOnRoom():Boolean{
        return isRoom
    }

    fun setOnRoom(isOn:Boolean){
        this.isRoom = isOn
    }


    override fun initService(service: AccessibilityService) {
        super.initService(service)
    }

    fun doLive(software: String, zhiboNum: String) {
        if(TextUtils.equals(Constants.Task.douyin,software)){
            ObserverManager.instance.add(Constants.Task.task3,this)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.Default) {
                        httpGetURL(zhiboNum)
                    }.let {
                        LogUtils.logGGQ("真是URL：${it}")
                        val id:String = TaskUtils.subRoomId(it)
                        setRoomId(id)
                        startLiveRoom()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    MyApplication.instance.getUiHandler().sendMessage("doLive e:${e}")
                }
            }
        }else if(TextUtils.equals(Constants.Task.kuaishou,software)){

        }
    }

    // 过滤 SPACE_TIME 事件内的重复页面
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 3000

    private fun startLiveRoom() {
        val currentTime = System.currentTimeMillis()
        if(!getOnRoom() && !TextUtils.isEmpty(getRoomId()) && currentTime - lastClickTime > SPACE_TIME){
            val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("snssdk1128://live?room_id=${getRoomId()}"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            MyApplication.instance.startActivity(intent)
            setOnRoom(true)
            MyApplication.instance.getUiHandler().sendMessage("<<<直播间>>>")
        }else{
            setOnRoom(false)
        }
    }


    private fun httpGetURL(s:String):String{
        val url = URL(s)
        val conn:HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.responseCode
        val realUrl:String = conn.url.toString()
        conn.disconnect()
        return realUrl
    }

    override fun observer(content: String) {
        LogUtils.logGGQ("监听到页面：${content}")
        when(content) {
            Constants.Douyin.PAGE_MAIN -> {
                MyApplication.instance.getUiHandler().sendMessage("回到首页")
                setOnRoom(false)
                //如果在任务内回到首页,进入直播间
                startLiveRoom()
            }

            Constants.Douyin.PAGE_LIVE_ROOM ->{
                MyApplication.instance.getUiHandler().sendMessage("进入直播间")
                setOnRoom(true)
            }
        }
    }


}