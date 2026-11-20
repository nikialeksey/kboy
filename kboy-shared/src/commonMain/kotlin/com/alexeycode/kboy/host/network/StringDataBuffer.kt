package com.alexeycode.kboy.host.network

class StringDataBuffer(
    private val stringDataMessage: DataMessage
) : SocketInputSession.Buffer {
    override fun isNotEmpty(): Boolean {
        return true
    }

    override fun message(): DataMessage {
        return stringDataMessage
    }
}