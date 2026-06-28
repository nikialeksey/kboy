package com.alexeycode.kboy.gb.cpu.registers

interface Register {
    fun set(v: Int)
    fun get(): Int
}
