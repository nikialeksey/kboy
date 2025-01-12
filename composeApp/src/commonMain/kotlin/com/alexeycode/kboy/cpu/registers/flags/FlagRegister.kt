package com.alexeycode.kboy.cpu.registers.flags

interface FlagRegister {
    fun z(): Flag
    fun n(): Flag
    fun h(): Flag
    fun c(): Flag
}