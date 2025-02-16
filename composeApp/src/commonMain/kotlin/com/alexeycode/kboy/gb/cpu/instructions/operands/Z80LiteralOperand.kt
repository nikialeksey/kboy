package com.alexeycode.kboy.gb.cpu.instructions.operands

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Z80LiteralOperand : Operand {
    private val name: String

    constructor(
        name: String,
    ) {
        this.name = name
    }

    override fun read8(memory: Memory, registers: Registers): Int {
        return name.substringBefore("H").toInt(16)
    }

    override fun write8(memory: Memory, registers: Registers, value: Int) {
        throw IllegalStateException("You can not write to literal operand!")
    }

    override fun read16(memory: Memory, registers: Registers): Int {
        return name.substringBefore("H").toInt(16)
    }

    override fun write16(memory: Memory, registers: Registers, value: Int) {
        throw IllegalStateException("You can not write to literal operand!")
    }

    override fun incOrDec(registers: Registers) {
        throw IllegalStateException("You can not inc/dec to literal operand!")
    }

    override fun toString(): String {
        return name
    }
}