package com.alexeycode.kboy.host.network

import io.ktor.client.HttpClient

interface ClientSocket :
    SocketInputSession,
    SocketOutputSession {
    suspend fun start(
        host: String,
        port: Int,
        path: String,
    ): HttpClient
    fun stop()
}
