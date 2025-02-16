package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta

interface Instructions {
    fun loadInstruction(address: Int)

    fun nextAddress(): Int
    fun isExtInstruction(): Boolean
    fun instructionMeta(): InstructionMeta
}