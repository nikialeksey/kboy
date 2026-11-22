package com.alexeycode.kboy.host.network

import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.CoroutineScope

class SimpleServerSocket(
    private val scope: CoroutineScope,
) : ServerSocket {

    private var server: EmbeddedServer<out ApplicationEngine, out ApplicationEngine.Configuration>? = null
    private var socketSession: SimpleSocketSession? = null

    override suspend fun start(
        path: String,
    ): EmbeddedServer<out ApplicationEngine, out ApplicationEngine.Configuration> {
        this.server?.stopSuspend()
        val server = embeddedServer(platformServerEngineFactory(), port = 0) {
            install(WebSockets)

            routing {
                webSocket(path = path) {
                    socketSession = SimpleSocketSession(this, scope, "server")
                    socketSession?.start()
                }
            }
        }.startSuspend()
        this.server = server
        return server
    }

    override fun stop() {
        server?.stop()
        server = null
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
