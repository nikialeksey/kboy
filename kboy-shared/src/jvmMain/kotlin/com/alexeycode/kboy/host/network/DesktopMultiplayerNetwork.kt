package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.host.network.multiplayer.Host
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

class DesktopMultiplayerNetwork : MultiplayerNetwork {

    private val hosts = MutableSharedFlow<Set<Host>>(1, 0, BufferOverflow.DROP_OLDEST)
    private lateinit var jmDns: JmDNS
    private val jmDnsServiceType = "_kboy._tcp.local."
    private lateinit var jmDnsServiceInfo: ServiceInfo

    private var server: EmbeddedServer<out ApplicationEngine, out ApplicationEngine.Configuration>? = null

    override fun start() {
        val server = embeddedServer(Netty, port = 0) {
            install(WebSockets)

            routing {
                webSocket("/") {
                }
            }
        }.start()
        this.server?.stop()
        this.server = server
        val serverPort = runBlocking { server.engine.resolvedConnectors().first().port }
        println("!!!!!!!!!!!!!!!!!!!!!! start webSocket on a port $serverPort")

        jmDns = JmDNS.create()
        try {
            jmDnsServiceInfo = ServiceInfo.create(jmDnsServiceType, "kboy", serverPort, "")
            jmDns.registerService(jmDnsServiceInfo)
        } catch (ignored: IOException) {
        }
    }

    override fun hosts(): Flow<Set<Host>> {
        return hosts.asSharedFlow()
    }

    override fun stop() {
        server?.stop()
        server = null
        if (::jmDnsServiceInfo.isInitialized) {
            jmDns.unregisterService(jmDnsServiceInfo)
        }
        jmDns.close()
    }
}
