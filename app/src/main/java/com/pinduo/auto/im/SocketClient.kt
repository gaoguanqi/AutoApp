package com.pinduo.auto.im

import com.google.gson.Gson
import com.pinduo.auto.app.config.Config
import com.pinduo.auto.http.entity.TaskEntity
import com.pinduo.auto.utils.LogUtils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import java.net.URISyntaxException

class SocketClient private constructor(){

    private val EVENT_TASK:String = "task"
    private var socket:Socket? = null
    private var listener:OnSocketListener? = null

    companion object{
        val instance: SocketClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            SocketClient()
        }
    }

    init {
        val opts:IO.Options = IO.Options()
        opts.reconnectionDelay = 1000L
        opts.transports = arrayOf(WebSocket.NAME)
        try {
            socket = IO.socket(Config.IO_SERVER_URL,opts)
        }catch (e: URISyntaxException){
            e.printStackTrace()
        }
        socket?.on(Socket.EVENT_CONNECT,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_CONNECTING,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_DISCONNECT,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_CONNECT_TIMEOUT,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_ERROR,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_RECONNECT,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_RECONNECTING,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_RECONNECT_FAILED,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(Socket.EVENT_RECONNECT_ERROR,object : Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })?.on(EVENT_TASK,object :Emitter.Listener{
            override fun call(vararg args: Any?) {
                try {
                    args?.let {
                        val arg = it[0]
                        arg?.let {
                            val taskEntity:TaskEntity = Gson().fromJson<TaskEntity>(it.toString(),TaskEntity::class.java)
                            listener?.let { it.call(taskEntity) }?:let {  LogUtils.logGGQ("listener is null") }
                        }?:let {
                            LogUtils.logGGQ("arg is null")
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    LogUtils.logGGQ("${EVENT_TASK} error -->>>${e.printStackTrace()}")
                }
            }
        })
    }

    fun setListener(listener: OnSocketListener){
        this.listener = listener
    }

    fun onConnect(){
        socket?.connect()
    }

}