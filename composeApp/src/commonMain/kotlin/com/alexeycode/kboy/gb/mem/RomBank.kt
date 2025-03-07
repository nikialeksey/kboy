package com.alexeycode.kboy.gb.mem

class RomBank(
    private val data: Array<Int>
) : Rom {
    override fun read8(address: Int): Int {
        return data[address]
    }
}