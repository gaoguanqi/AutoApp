package com.pinduo.auto.app

import android.app.Application
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.pinduo.auto.app.manager.AppLifeCycleCallBack
import com.pinduo.auto.utils.UiHandler

class MyApplication:Application() {

    private lateinit var uiHandler:UiHandler
    companion object {
        @JvmStatic
        lateinit var instance: MyApplication
            private set
    }

    fun getUiHandler():UiHandler{
        return uiHandler
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        this.uiHandler = UiHandler()
        initConfig()
    }

    private fun initConfig() {
        Utils.init(this)
        SPUtils.getInstance(AppUtils.getAppPackageName())
        registerActivityLifecycleCallbacks(AppLifeCycleCallBack())
    }
}