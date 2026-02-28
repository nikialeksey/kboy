package com.alexeycode.kboy.gb.mem

interface Rom {
    fun read8(address: Int): Int
}