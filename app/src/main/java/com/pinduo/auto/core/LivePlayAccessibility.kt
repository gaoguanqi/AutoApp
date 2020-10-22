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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class LivePlayAccessibility private constructor():BaseAccessbility() {

    companion object {
        val INSTANCE:LivePlayAccessibility by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            LivePlayAccessibility()
        }
    }

    override fun initService(service: AccessibilityService) {
        super.initService(service)
    }


    fun doLive(software: String, zhiboNum: String) {
        if(TextUtils.equals(Constants.Task.douyin,software)){
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.Default) {
                        httpGetURL(zhiboNum)
                    }.let {
                        LogUtils.logGGQ("真是URL：${it}")
                        val roomId:String = TaskUtils.subRoomId(it)
                        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("snssdk1128://live?room_id=${roomId}"))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        MyApplication.instance.startActivity(intent)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }else if(TextUtils.equals(Constants.Task.kuaishou,software)){

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



}