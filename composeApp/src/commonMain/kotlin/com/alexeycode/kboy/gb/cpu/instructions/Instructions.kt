package com.alexeycode.kboy.gb.cpu.instructions

interface Instructions {
    fun instruction(address: Int): ReadInstruction
}