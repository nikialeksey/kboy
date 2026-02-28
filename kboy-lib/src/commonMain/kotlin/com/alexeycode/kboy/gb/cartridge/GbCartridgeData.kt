package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Rom

class GbCartridgeData : Rom {

    private val dataStream: ByteArray

    constructor(dataStream: ByteArray) {
        this.dataStream = dataStream
    }

    override fun read8(address: Int): Int {
        return dataStream[address].toUByte().toInt() // TODO place for optimization
    }
}