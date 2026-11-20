package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.gb.serial.OutputWire

class SocketOutputWire(
    private val session: SocketOutputSession,
) : OutputWire {

    private var wasTransferred = false
    private var pkt: Int = 0
    private val ackListener = { ackPkt: Int ->
        if (ackPkt == pkt) { wasTransferred = true }
    }

    override fun send(data: Int) {
        pkt++
        wasTransferred = false
        session.send(data, pkt)
        session.addAckListener(ackListener)
    }

    override fun wasTransferred(): Boolean {
        return wasTransferred
    }

    override fun reset() {
        wasTransferred = false
        session.removeAckListener(ackListener)
    }
}