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

    }

    object BundleKey {

    }
}