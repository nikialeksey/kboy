package com.alexeycode.kboy.cpu.opcodes

interface Cycles {
    fun action(): Int
    fun none(): Int
}