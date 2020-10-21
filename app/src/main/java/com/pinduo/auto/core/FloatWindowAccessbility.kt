package com.pinduo.auto.core

import android.accessibilityservice.AccessibilityService

class FloatWindowAccessbility private constructor():BaseAccessbility() {

    companion object {
        val INSTANCE:FloatWindowAccessbility by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            FloatWindowAccessbility()
        }
    }

    override fun initService(service: AccessibilityService) {
        super.initService(service)
    }





}