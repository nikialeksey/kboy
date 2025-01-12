package com.alexeycode.kboy.cpu.opcodes

interface Opcodes {
    fun instructionMeta(code: Int): InstructionMeta
    fun cbInstructionMeta(code: Int): InstructionMeta
}