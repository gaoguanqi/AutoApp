package com.pinduo.auto.utils

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



    }
}