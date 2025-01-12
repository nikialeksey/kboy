package com.alexeycode.kboy.cpu.registers

interface Register {
    fun bytes(): Int
    fun set(v: Int)
    fun get(): Int
}