package com.alexeycode.kboy.cartridge

class GbHeader(
    private val data: IntArray
) : Header {

    private val logo: Logo by lazy {
        TODO() // GbLogo(data.sliceArray(0x04..0x33))
    }
    private val name: String by lazy {
        data.slice(0x34 until 0x43)
            .dropLastWhile { it == 0x00 }
            .map { it.toByte() }
            .toByteArray()
            .decodeToString()
            .trim()
    }

    override fun logo(): Logo {
        return logo
    }

    override fun name(): String {
        return name
    }

}