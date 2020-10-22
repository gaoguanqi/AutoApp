package com.pinduo.auto.http.entity

data class TaskEntity(val code:Int = 0,
                      val status:Int = 0,
                      val message:String = "",
                      val device:String = "",
                      val zx_time:String = "",
                      val software:String = "",
                      val task:String = "",
                      val min_time:String = "",
                      val max_time:String = "",
                      val baifenbi:String = "",
                      val fayan:String = "",
                      val apk_url:String = "",
                      val apk_version_code:String = "",
                      val other_version_name:String = "",
                      val other_url:String = "",
                      val sw_id_url:String = "",
                      val content:String = "",
                      val interval:String = "",
                      val zhibo_num:String = ""
)