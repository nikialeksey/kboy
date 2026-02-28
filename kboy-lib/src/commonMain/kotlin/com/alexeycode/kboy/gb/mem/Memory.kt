package com.alexeycode.kboy.gb.mem

interface Memory : Rom {
    fun write8(address: Int, value: Int)
}