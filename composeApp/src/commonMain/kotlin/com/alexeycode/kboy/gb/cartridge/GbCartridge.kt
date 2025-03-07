package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.Rom
import com.alexeycode.kboy.gb.mem.SimpleMemory

class GbCartridge(
    private val data: Rom,
    private val meta: CartridgeMeta
) : Cartridge {

    private val bytes by lazy {
        val size = meta.romSizeKb() * 1024
        val bytesArray = Array(0xFFFF + 1) { 0 }

        for (address in 0 until size) {
            bytesArray[address] = data.read8(address)
        }

        bytesArray
    }

    private val memory by lazy {
        SimpleMemory(bytes)
    }

    constructor(data: Rom) : this(data, GbCartridgeMeta(data))

    override fun memory(): Memory {
        return memory
    }

    override fun vram(): Memory {
        TODO()
    }

}