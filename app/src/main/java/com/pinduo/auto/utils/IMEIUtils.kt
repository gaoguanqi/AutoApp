package com.pinduo.auto.utils

import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.pinduo.auto.app.MyApplication
import com.pinduo.auto.app.global.Constants


class IMEIUtils {

    companion object{
        private var imei:String = ""

        fun getIMEI():String{
            return imei
        }

        fun setIMEI(v:String){
            imei = v
        }

        fun getDeviceId():String?{
            return DeviceUtils.getAndroidID()
        }

//        fun readIMEI2File(){
//            FileIOUtils.readFile2String(Constants.Path.IMEI_PATH)
//        }
//
//        fun writeIMEI2File(imei:String):Boolean{
//            if(FileUtils.createOrExistsFile(Constants.Path.IMEI_PATH)){
//               return FileIOUtils.writeFileFromString(Constants.Path.IMEI_PATH,imei)
//            }
//            return false
//        }


    }
}