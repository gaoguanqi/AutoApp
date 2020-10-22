package com.pinduo.auto.app.global

import android.os.Environment


class Constants {
    object Path{
        val path = Environment.getExternalStorageDirectory().path
        val BASE_PATH:String = path + "/auto"
        val IMEI_PATH = BASE_PATH+"/imei.txt"
    }

    object ApiParams{
        const val USERNAME = "username"
        const val IMEI = "imei"
    }
    object SaveInfoKey {

    }

    object GlobalValue {
        const val douyinPackage = "com.ss.android.ugc.aweme"
    }

    object BundleKey {

    }


    object Task{
        const val douyin = "1"
        const val kuaishou = "2"

        const val task1 = "1"
        const val task2 = "2"
        const val task3 = "3"



    }
}