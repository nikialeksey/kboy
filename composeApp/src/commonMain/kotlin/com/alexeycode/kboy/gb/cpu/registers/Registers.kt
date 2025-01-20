package com.alexeycode.kboy.gb.cpu.registers

import com.alexeycode.kboy.gb.cpu.registers.flags.FlagRegister

interface Registers {
    fun a(): Register
    fun f(): Register
    fun b(): Register
    fun c(): Register
    fun d(): Register
    fun e(): Register
    fun h(): Register
    fun l(): Register
    fun af(): Register
    fun bc(): Register
    fun de(): Register
    fun hl(): Register
    fun sp(): Register
    fun pc(): Register
    fun flag(): FlagRegister
    fun byName(name: String): Register
}