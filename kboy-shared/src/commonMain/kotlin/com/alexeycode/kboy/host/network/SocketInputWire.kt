package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.gb.serial.InputWire

class SocketInputWire(
    private val session: SocketInputSession,
) : InputWire {

    private var dataBuffer = mutableListOf<Int>()

    override fun wasReceived(): Boolean {
        val buffer = session.buffer()
        if (buffer.isNotEmpty()) {
            val message = buffer.message()
            dataBuffer.add(message.data())
            session.sendAck(message.pkt())
        }
        return dataBuffer.isNotEmpty()
    }

    override fun get(): Int {
        return dataBuffer.first()
    }

    override fun reset() {
        dataBuffer.clear()
        session.clearBuffer()
    }
}
