package com.alexeycode.kboy.mem

interface Memory : Rom {
    fun write8(address: Int, value: Int)
}