package com.alexeycode.kboy.cpu.instructions.operands

import com.alexeycode.kboy.cpu.registers.Registers
import com.alexeycode.kboy.mem.Memory

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

    override fun check(registers: Registers): Boolean {
        return if (name == "NZ") {
            !registers.flag().z().isEnabled()
        } else if (name == "Z") {
            registers.flag().z().isEnabled()
        } else if (name == "NC") {
            !registers.flag().c().isEnabled()
        } else if (name == "C") {
            registers.flag().c().isEnabled()
        } else {
            throw IllegalArgumentException(
                "You tried to check unknown flag '$name'!"
            )
        }
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