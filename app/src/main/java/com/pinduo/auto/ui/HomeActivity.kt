package com.pinduo.auto.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import com.pinduo.auto.R
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.base.BaseActivity
import com.pinduo.auto.utils.AccessibilityServiceUtils
import com.pinduo.auto.utils.LogUtils
import com.pinduo.auto.utils.ToastUtil
import com.yhao.floatwindow.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    val REQUESTCODE_ACCESSIBILITY:Int = 1001

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initData(savedInstanceState: Bundle?) {
        sw_float_window.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                FloatWindow.get()?.let {
                    if(!it.isShowing) it.show()
                }
            }else{
                FloatWindow.get()?.let {
                    if(it.isShowing) it.hide()
                }
            }
        }
        checkAccessibilityPermission()
    }

    @SuppressLint("InflateParams")
    private fun initFloatWindow() {
        val view: View = this.layoutInflater.inflate(R.layout.layout_float_view,null)
        FloatWindow.with(getApplicationContext())
            .setView(view)
            .setWidth(Screen.width, 0.4f)                               //设置控件宽高
            .setHeight(Screen.height, 0.2f)
            .setX(2)                                   //设置控件初始位置
            .setY(2)
            .setDesktopShow(true)                        //桌面显示
            .setMoveType(MoveType.back) //可拖动，释放后自动回到原位置
            .setViewStateListener(MyViewStateListener())    //监听悬浮控件状态改变
            .setPermissionListener(MyPermissionListener())
            .build()
        FloatWindow.get()?.let {
            if(!it.isShowing)it.show()
        }
    }

    inner class MyViewStateListener: ViewStateListener{
        override fun onBackToDesktop() {
        }

        override fun onMoveAnimStart() {
        }

        override fun onMoveAnimEnd() {
        }

        override fun onPositionUpdate(x: Int, y: Int) {
        }

        override fun onDismiss() {
        }

        override fun onShow() {
            FloatWindow.get()?.view?.let {
                MyApplication.instance.getUiHandler().initFloater(it.findViewById(R.id.sv_container),it.findViewById(R.id.tv_msg))
            }
        }

        override fun onHide() {
        }
    }

    inner class MyPermissionListener: PermissionListener {
        override fun onSuccess() {

        }

        override fun onFail() {

        }
    }

    private fun checkAccessibilityPermission(){
        if(AccessibilityServiceUtils.isAccessibilitySettingsOn(MyApplication.instance)){
            LogUtils.logGGQ("无障碍已开启")
            initFloatWindow()
        }else{
            LogUtils.logGGQ("无障碍未开启")
            startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),REQUESTCODE_ACCESSIBILITY)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.logGGQ("requestCode:${requestCode} -- resultCode:${resultCode}")
        if(requestCode == REQUESTCODE_ACCESSIBILITY){
            checkAccessibilityPermission()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.moveTaskToBack(true)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        FloatWindow.destroy()
    }
}