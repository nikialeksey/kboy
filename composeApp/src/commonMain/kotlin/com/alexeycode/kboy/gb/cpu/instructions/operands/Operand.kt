package com.alexeycode.kboy.gb.cpu.instructions.operands

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

interface Operand {
    fun read8(memory: Memory, registers: Registers): Int
    fun write8(memory: Memory, registers: Registers, value: Int)
    fun read16(memory: Memory, registers: Registers): Int
    fun write16(memory: Memory, registers: Registers, value: Int)
    fun incOrDec(registers: Registers)

    class Dummy : Operand {
        override fun read8(memory: Memory, registers: Registers): Int {
            throw IllegalArgumentException()
        }

        override fun write8(memory: Memory, registers: Registers, value: Int) {
            // ignored
        }

        override fun read16(memory: Memory, registers: Registers): Int {
            throw IllegalArgumentException()
        }

        override fun write16(memory: Memory, registers: Registers, value: Int) {
            // ignored
        }

        override fun incOrDec(registers: Registers) {
            // ignored
        }
    }
}