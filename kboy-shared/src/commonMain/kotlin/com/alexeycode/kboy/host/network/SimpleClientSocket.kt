package com.alexeycode.kboy.host.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

class SimpleClientSocket(
    private val scope: CoroutineScope
) : ClientSocket {

    private var client: HttpClient? = null
    private var socketSession: SimpleSocketSession? = null

    override suspend fun start(host: String, port: Int, path: String): HttpClient {
        client?.close()
        val client = HttpClient(platformClientEngineFactory()) {
            install(WebSockets)
        }
        this.client = client

        val started = Semaphore(permits = 1, acquiredPermits = 1)
        scope.launch {
            client.webSocket(method = HttpMethod.Get, host = host, port = port, path = path) {
                socketSession = SimpleSocketSession(this, scope, "client")
                socketSession?.start()
                started.release()
            }
        }
        started.acquire()

        return client
    }

    override fun stop() {
        client?.close()
        client = null
        socketSession = null
    }

    override fun buffer(): SocketInputSession.Buffer {
        return socketSession?.buffer() ?: SocketInputSession.Buffer.Empty
    }

    override fun clearBuffer() {
        socketSession?.clearBuffer()
    }

    override fun sendAck(pkt: Int) {
        socketSession?.sendAck(pkt)
    }

    override fun send(data: Int, pkt: Int) {
        socketSession?.send(data, pkt)
    }

    override fun addAckListener(listener: (Int) -> Unit) {
        socketSession?.addAckListener(listener)
    }

    override fun removeAckListener(listener: (Int) -> Unit) {
        socketSession?.removeAckListener(listener)
    }
}