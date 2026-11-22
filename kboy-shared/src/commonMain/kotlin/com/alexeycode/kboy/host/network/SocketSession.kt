package com.alexeycode.kboy.host.network

interface SocketSession :
    SocketInputSession,
    SocketOutputSession {
    suspend fun start()
    fun stop()
}
