package com.alexeycode.kboy.gb.cpu.opcodes

interface InstructionMeta {
    fun opcode(): Int
    fun isImmediate(): Boolean
    fun cycles(): Cycles
    fun bytes(): Int
    fun mnemonic(): String
    fun operands(): List<OperandMeta>
    fun flags(): Map<String, String>
}