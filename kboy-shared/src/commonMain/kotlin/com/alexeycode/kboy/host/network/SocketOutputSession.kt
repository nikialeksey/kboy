package com.alexeycode.kboy.host.network

interface SocketOutputSession {
    fun send(data: Int, pkt: Int)
    fun addAckListener(listener: (Int) -> Unit)
    fun removeAckListener(listener: (Int) -> Unit)
}