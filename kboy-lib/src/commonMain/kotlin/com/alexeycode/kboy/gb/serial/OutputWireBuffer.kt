package com.alexeycode.kboy.gb.serial

class OutputWireBuffer : OutputWire {

    private val buffer = mutableListOf<Byte>()
    private var wasTransferred = false

    override fun send(data: Int) {
        buffer.add(data.toByte())
        wasTransferred = true
    }

    override fun wasTransferred(): Boolean {
        return wasTransferred
    }

    override fun reset() {
        wasTransferred = false
    }

    fun outputData(): ByteArray {
        return buffer.toByteArray()
    }
}
