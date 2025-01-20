package com.alexeycode.kboy.gb.cpu.opcodes

interface Cycles {
    fun action(): Int
    fun none(): Int
}