package com.pinduo.auto.core

import android.accessibilityservice.AccessibilityService
abstract class BaseAccessbility {

    lateinit var service: AccessibilityService

    open fun initService(service:AccessibilityService){
        this.service = service
    }

}