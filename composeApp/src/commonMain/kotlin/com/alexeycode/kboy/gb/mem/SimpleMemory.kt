package com.alexeycode.kboy.gb.mem

class SimpleMemory(
    private val data: Array<Int>
) : Memory {

    override fun write8(address: Int, value: Int) {
        data[address] = value
    }

    override fun read8(address: Int): Int {
        return data[address]
    }
}