package com.pinduo.auto.core

import android.accessibilityservice.AccessibilityService

class LivePlayAccessibility private constructor():BaseAccessbility() {

    companion object {
        val INSTANCE:LivePlayAccessibility by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            LivePlayAccessibility()
        }
    }

    override fun initService(service: AccessibilityService) {
        super.initService(service)
    }


    fun doLive() {

    }


}