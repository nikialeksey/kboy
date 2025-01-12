package com.alexeycode.kboy.cpu.instructions.operands

import com.alexeycode.kboy.cpu.registers.Registers
import com.alexeycode.kboy.mem.Memory

interface Operand {
    fun read8(memory: Memory, registers: Registers): Int
    fun write8(memory: Memory, registers: Registers, value: Int)
    fun read16(memory: Memory, registers: Registers): Int
    fun write16(memory: Memory, registers: Registers, value: Int)
    fun incOrDec(registers: Registers)
    fun check(registers: Registers): Boolean
}