package com.pinduo.auto.utils

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
//b2c
class NodeUtil {
    companion object{

        fun getRootNode(service: AccessibilityService): AccessibilityNodeInfo? {
            return service.rootInActiveWindow
        }


        fun findNodeById(service: AccessibilityService,id:String):MutableList<AccessibilityNodeInfo>{
            val targetNodes:MutableList<AccessibilityNodeInfo> = mutableListOf()

            getRootNode(service)?.let {
//                for(index in 0 until it.childCount){
//                    val item:AccessibilityNodeInfo? = it.getChild(index)
//                    if(item != null){
//                       val items = item.findAccessibilityNodeInfosByViewId(id)
//                        if(!items.isNullOrEmpty()){
//                            targetNodes.addAll(items)
//                        }
//                    }else{
//                        findNodeById(service,id)
//                    }
//                }




                val nodes = it.findAccessibilityNodeInfosByViewId(id)
                it.recycle()
                for(index in 0 until it.childCount){

                }
            }
            return targetNodes
        }

//        fun findNodeByClassName(node:AccessibilityNodeInfo,className:String):MutableList<AccessibilityNodeInfo>{
//            val targetNodes:MutableList<AccessibilityNodeInfo> = mutableListOf()
//
//            for(index in 0 until node.childCount){
//               val c:AccessibilityNodeInfo? = node.getChild(index)
//                c?.let {
//                    if(TextUtils.equals(className,c.className)){
//                        targetNodes.add(it)
//                    }else{
//                        findNodeByClassName(it,className)
//                    }
//                }
//            }
//            return targetNodes
//        }

//        fun addTxt2Edit(service: AccessibilityService,txt:String):Boolean{
//            getRootNode(service)?.let {
//                val nodes = findNodeByClassName(it,"android.widget.EditText")
//                if(!nodes.isEmpty()){
//                    nodes.forEach {
//                        val arguments: Bundle = Bundle()
//                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt)
//                        return it.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments)
//                    }
//                }
//            }
//            return false
//        }

    }
}