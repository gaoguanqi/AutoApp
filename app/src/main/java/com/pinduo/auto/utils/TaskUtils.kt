package com.pinduo.auto.utils

import android.text.TextUtils
import kotlin.random.Random

class TaskUtils{
    companion object{
        ///截取抖音直播的 room id
        fun subRoomId(url:String):String{
            val reflowIndex:Int = url.indexOf("reflow/")
            val uCode:Int = url.indexOf("u_code")
            val roomId:String = url.substring(reflowIndex + 7,uCode - 1)
            LogUtils.logGGQ("直播间id：${roomId}")
            return roomId
        }


        ///截取评论内容
        fun handContent(content:String):String{
            var newContent:String = "~"
            if(TextUtils.isEmpty(content)) return newContent
            if(content.contains(";")){
                content.split(";").let {
                    newContent = it[Random.nextInt(it.size)]
                }
            }
            return newContent
        }
    }
}