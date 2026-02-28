package com.alexeycode.kboy.gb.serial

class BufferSerial : Serial {

    private val buffer = mutableListOf<Int>()

    override fun put(data: Int) {
        buffer.add(data)
    }

    fun asByteArray(): ByteArray {
        return ByteArray(buffer.size) { i -> buffer[i].toByte() }
    }
}