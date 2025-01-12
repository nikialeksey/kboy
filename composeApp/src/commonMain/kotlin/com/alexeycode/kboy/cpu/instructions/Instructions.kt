package com.alexeycode.kboy.cpu.instructions

interface Instructions {
    fun instruction(address: Int): ReadInstruction
}