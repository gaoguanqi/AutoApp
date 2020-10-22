package com.pinduo.auto.core

import android.accessibilityservice.AccessibilityService

class CommonAccessbility private constructor():BaseAccessbility() {

    companion object {
        val INSTANCE:CommonAccessbility by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            CommonAccessbility()
        }
    }

    override fun initService(service: AccessibilityService) {
        super.initService(service)
    }





}