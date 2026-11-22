package com.alexeycode.kboy.gb.cartridge

class GbHeader(
    private val data: IntArray,
) : Header {

    private val name: String by lazy {
        data.slice(0x34 until 0x43)
            .dropLastWhile { it == 0x00 }
            .map { it.toByte() }
            .toByteArray()
            .decodeToString()
            .trim()
    }

    override fun name(): String {
        return name
    }
}
