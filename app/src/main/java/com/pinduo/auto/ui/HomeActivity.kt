package com.pinduo.auto.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import com.pinduo.auto.R
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.base.BaseActivity
import com.pinduo.auto.utils.AccessibilityServiceUtils
import com.pinduo.auto.utils.LogUtils
import com.yhao.floatwindow.FloatWindow
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    val REQUESTCODE_ACCESSIBILITY: Int = 1001

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initData(savedInstanceState: Bundle?) {
        sw_float_window.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                FloatWindow.get()?.let {
                    if (!it.isShowing) it.show()
                }
            } else {
                FloatWindow.get()?.let {
                    if (it.isShowing) it.hide()
                }
            }
        }
        checkAccessibilityPermission()
    }


    private fun checkAccessibilityPermission() {
        if (AccessibilityServiceUtils.isAccessibilitySettingsOn(MyApplication.instance)) {
            LogUtils.logGGQ("无障碍已开启")
            FloatWindow.get()?.let {
                if (!it.isShowing) it.show()
            }
        } else {
            LogUtils.logGGQ("无障碍未开启")
            startActivityForResult(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                REQUESTCODE_ACCESSIBILITY
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.logGGQ("requestCode:${requestCode} -- resultCode:${resultCode}")
        if (requestCode == REQUESTCODE_ACCESSIBILITY) {
            checkAccessibilityPermission()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.moveTaskToBack(true)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        FloatWindow.destroy()
    }
}