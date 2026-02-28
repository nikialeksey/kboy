package com.alexeycode.kboy.gb.cpu.registers

interface Register {
    fun bytes(): Int
    fun set(v: Int)
    fun get(): Int
}