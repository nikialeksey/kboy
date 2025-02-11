package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.Rom

class GbCartridge(
    private val data: Rom,
    private val meta: CartridgeMeta
) : Cartridge {

    constructor(data: Rom) : this(data, GbCartridgeMeta(data))

    override fun upload(to: Memory) {
        val upperBound = meta.romSizeKb() * 1024
        for (address in 0x0000 until upperBound) {
            to.write8(address, data.read8(address))
        }
    }

}