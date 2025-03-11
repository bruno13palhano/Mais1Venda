package com.bruno13palhano.data.datasource.remote

import android.util.Log
import com.bruno13palhano.data.BuildConfig
import javax.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

internal class WebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : WebSocketClientInterface {
    private var webSocket: WebSocket? = null
    private var messageListener: ((String) -> Unit)? = null
    private var isConnected = false

    override fun connect() {
        if (isConnected) return

        val serverSocket = BuildConfig.ServerSocket
        val request = Request.Builder()
            .url(serverSocket)
            .build()

        webSocket = okHttpClient.newWebSocket(
            request = request,
            object : okhttp3.WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.d("WebSocket", "Connection establish!")
                    isConnected = true
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d("WebSocket", "Received message: $text")
                    messageListener?.invoke(text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    Log.d("WebSocket", "Received bytes: $bytes")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d("WebSocket", "Closing connection: $reason")
                    webSocket.close(code = 1000, reason = null)
                    isConnected = false
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("WebSocket", "Error: ${t.message}")
                    t.printStackTrace()
                    isConnected = false
                }
            },
        )
    }

    override fun disconnect() {
        webSocket?.close(code = 1000, reason = "Disconnected by the app")
        isConnected = false
    }

    override fun setOnMessageListener(listener: (String) -> Unit) {
        this.messageListener = listener
    }
}

interface WebSocketClientInterface {
    fun connect()
    fun disconnect()
    fun setOnMessageListener(listener: (String) -> Unit)
}
