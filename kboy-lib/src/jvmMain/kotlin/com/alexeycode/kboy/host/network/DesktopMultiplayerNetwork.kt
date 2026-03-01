package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.host.network.multiplayer.Host
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

class DesktopMultiplayerNetwork : MultiplayerNetwork {

    private val hosts = MutableSharedFlow<List<Host>>(0, 1, BufferOverflow.DROP_OLDEST)
    private lateinit var jmDns: JmDNS
    private val jmDnsServiceType = "_kboy._tcp.local."
    private lateinit var jmDnsServiceInfo: ServiceInfo

    private var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    override fun start() {
        jmDns = JmDNS.create(InetAddress.getByName("0.0.0.0"))

        try {
            val server = embeddedServer(CIO, port = 0) {
                install(WebSockets)

                routing {
                    webSocket("/") {
                        for (frame in incoming) {
                            val message = frame.readBytes().toString()
                            send(Frame.Text("Hey, $message"))
                        }
                    }
                }
            }.start()
            this.server?.stop()
            this.server = server

            val port = runBlocking { server.engine.resolvedConnectors().first().port }
            println("!!!!!!!!!!!!!!!!!!!!!! start webSocket on a port $port")
            jmDnsServiceInfo = ServiceInfo.create(jmDnsServiceType, "kboy", port, "")
            jmDns.registerService(jmDnsServiceInfo)
        } catch (ignored: IOException) {
            // in this case we should disable multiplayer feature
        }
    }

    override fun hosts(): Flow<List<Host>> {
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
