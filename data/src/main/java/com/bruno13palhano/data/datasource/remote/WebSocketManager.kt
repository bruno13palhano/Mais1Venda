package com.bruno13palhano.data.datasource.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

class WebSocketManager(
    private val webSocketListener: WebSocketListener,
) {
    // extract to constructor
    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url("ws://SERVER:PORT")
            .build()

        webSocket = client.newWebSocket(
            request = request,
            object : okhttp3.WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.d("WebSocket", "Connection establish!")
                    webSocketListener.onConnected()
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d("WebSocket", "Received message: $text")
                    webSocketListener.onMessage(message = text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    Log.d("WebSocket", "Received bytes: $bytes")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d("WebSocket", "Closing connection: $reason")
                    webSocket.close(code = 1000, reason = null)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("WebSocket", "Error: ${t.message}")
                    webSocketListener.onError(error = t.message ?: "Unknown error")
                }
            },
        )
    }

    fun send(message: String) {
        webSocket?.send(text = message)
    }

    fun disconnect() {
        webSocket?.close(code = 1000, reason = "Disconnected by the app")
    }
}

interface WebSocketListener {
    fun onConnected()
    fun onMessage(message: String)
    fun onError(error: String)
}
