package com.pinduo.auto.http.entity

data class TaskEntity(val code:Int = 0,val status:Int,val message:String = "",val device:String = "",val zx_time:String = "")