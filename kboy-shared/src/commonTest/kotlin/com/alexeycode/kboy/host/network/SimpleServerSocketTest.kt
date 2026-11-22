package com.alexeycode.kboy.host.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.time.Duration.Companion.milliseconds

class SimpleServerSocketTest {
    @Test
    fun asdasd() = runTest {
        val path = "socket-path"
        val serverScope = CoroutineScope(Dispatchers.Default)
        val serverSocket = SimpleServerSocket(scope = serverScope)
        val server = serverSocket.start(path)
        val connectorConfig = server.engine.resolvedConnectors().first()
        val serverHost = connectorConfig.host
        val serverPort = connectorConfig.port
        println("!!!!!! server: $serverHost:$serverPort")

        val clientScope = CoroutineScope(Dispatchers.Default)
        val clientSocket = SimpleClientSocket(scope = clientScope)
        clientSocket.start(host = serverHost, port = serverPort, path = path)

        // there is no data yet
        assertFalse(clientSocket.buffer().isNotEmpty())
        serverSocket.send(1, 1)

        while (!clientSocket.buffer().isNotEmpty()) {
            serverSocket.send(1, 1)
            delay(100.milliseconds)
        }

        clientSocket.stop()
        serverSocket.stop()
    }
}
