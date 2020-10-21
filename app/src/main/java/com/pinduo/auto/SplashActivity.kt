package com.pinduo.auto

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.pinduo.auto.app.global.Constants
import com.pinduo.auto.base.BaseActivity
import com.pinduo.auto.ui.AccountActivity
import com.pinduo.auto.ui.HomeActivity
import com.pinduo.auto.utils.IMEIUtils
import com.pinduo.auto.utils.LogUtils
import com.pinduo.auto.utils.PermissionUtil
import com.pinduo.auto.utils.RequestPermission
import com.tbruyelle.rxpermissions2.RxPermissions

class SplashActivity : BaseActivity() {
    val rxPermissions: RxPermissions = RxPermissions(this)

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initData(savedInstanceState: Bundle?) {
        applyPermissions()
    }

    override fun onRestart() {
        super.onRestart()
        applyPermissions()
    }

    private fun applyPermissions() {
        PermissionUtil.applyPermissions(object : RequestPermission {
            override fun onRequestPermissionSuccess() {
                launchTarget()
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showTopMessage("权限未通过")
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                showTopMessage("权限未通过")
            }
        }, rxPermissions)
    }

    private fun launchTarget() {
        if (FileUtils.createOrExistsFile(Constants.Path.IMEI_PATH)) {
            val imei: String? = FileIOUtils.readFile2String(Constants.Path.IMEI_PATH)
            if (TextUtils.isEmpty(imei)) {
                startActivity(Intent(SplashActivity@ this, AccountActivity::class.java))
                finish()
            } else {
                LogUtils.logGGQ("imei:${imei}")
                IMEIUtils.setIMEI(imei!!)
                startActivity(Intent(SplashActivity@ this, HomeActivity::class.java))
                finish()
            }
        } else {
            startActivity(Intent(SplashActivity@ this, AccountActivity::class.java))
            finish()
        }
    }
}