package com.alexeycode.kboy.gb.mem

class PrintableMemory(
    private val origin: Memory
) : Memory {

    override fun write8(address: Int, value: Int) {
        origin.write8(address, value)
        println("Write to 0x${address.toHexWord()}: 0x${value.toHexByte()}")
    }

    override fun read8(address: Int): Int {
        val value = origin.read8(address)
        println("Read from 0x${address.toHexWord()}: 0x${value.toHexByte()}")
        return value
    }
}
