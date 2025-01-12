package com.alexeycode.kboy.mem

class PrintableMemory(
    private val origin: Memory
) : Memory {

    override fun write8(address: Int, value: Int) {
        origin.write8(address, value)
        println("Write to 0x${address.toString(16).padStart(4, padChar = '0').uppercase()}: 0x${value.toString(16).padStart(2, '0')}")
    }

    override fun read8(address: Int): Int {
        val value = origin.read8(address)
        println("Read from 0x${address.toString(16).padStart(4, padChar = '0').uppercase()}: 0x${value.toString(16).padStart(2, '0')}")
        return value
    }
}