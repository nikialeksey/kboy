package com.alexeycode.kboy.host.network

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer

interface ServerSocket : SocketInputSession, SocketOutputSession {
    suspend fun start(path: String): EmbeddedServer<out ApplicationEngine, out ApplicationEngine.Configuration>
    fun stop()
}