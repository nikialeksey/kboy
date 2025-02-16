package com.alexeycode.kboy.gb.cpu.instructions.operands

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Z80FlagOperand : Operand {
    private val name: String
    private val isImmediate: Boolean

    constructor(name: String, isImmediate: Boolean) {
        this.name = name
        this.isImmediate = isImmediate
    }

    override fun read8(memory: Memory, registers: Registers): Int {
        throw IllegalStateException(
            "You can not read from flag operands!"
        )
    }

    override fun write8(memory: Memory, registers: Registers, value: Int) {
        throw IllegalStateException(
            "You can not write to flag operands!"
        )
    }

    override fun read16(memory: Memory, registers: Registers): Int {
        throw IllegalStateException(
            "You can not read from flag operands!"
        )
    }

    override fun write16(memory: Memory, registers: Registers, value: Int) {
        throw IllegalStateException(
            "You can not write to flag operands!"
        )
    }

    override fun incOrDec(registers: Registers) {
        throw IllegalStateException(
            "You can not inc/dec to flag operands!"
        )
    }

    override fun toString(): String {
        val v = name
        return if (isImmediate) {
            v
        } else {
            "($v)"
        }
    }
}