package com.alexeycode.kboy.host.network

interface SocketInputSession {
    fun buffer(): Buffer
    fun clearBuffer()
    fun sendAck(pkt: Int)

    interface Buffer {
        fun isNotEmpty(): Boolean
        fun message(): DataMessage

        object Empty : Buffer {
            override fun isNotEmpty(): Boolean {
                return false
            }

            override fun message(): DataMessage {
                throw IllegalArgumentException("Empty buffer does not contain a message.")
            }

        }
    }
}