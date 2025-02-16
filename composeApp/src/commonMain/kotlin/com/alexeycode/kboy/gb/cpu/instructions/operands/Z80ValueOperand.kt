package com.alexeycode.kboy.gb.cpu.instructions.operands

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Z80ValueOperand : Operand {
    private val value: Int
    private val bytesCount: Int
    private val isImmediate: Boolean

    constructor(value: Int, bytesCount: Int, isImmediate: Boolean) {
        this.value = value
        this.bytesCount = bytesCount
        this.isImmediate = isImmediate
    }

    override fun read8(memory: Memory, registers: Registers): Int {
        return if (isImmediate) {
            value
        } else {
            memory.read8(value)
        }
    }

    override fun write8(memory: Memory, registers: Registers, value: Int) {
        if (isImmediate) {
            throw IllegalStateException(
                "You can not write values to the value immediate operands!"
            )
        } else {
            if (bytesCount == 1) {
                memory.write8(0xFF00 + this.value, value)
            } else {
                memory.write8(this.value, value)
            }
        }
    }

    override fun read16(memory: Memory, registers: Registers): Int {
        return if (isImmediate) {
            value
        } else {
            throw IllegalStateException(
                "You can not read word from memory by value operand!"
            )
        }
    }

    override fun write16(memory: Memory, registers: Registers, value: Int) {
        if (isImmediate) {
            throw IllegalStateException(
                "You can not write word into immediate value operand!"
            )
        } else {
            memory.write8(this.value, value.and(0xFF))
            memory.write8(this.value + 1, value.shr(8).and(0xFF))
        }
    }

    override fun incOrDec(registers: Registers) {
        throw IllegalStateException(
            "You can not inc/dec value operands!"
        )
    }

    override fun toString(): String {
        val v = "0x${value.toString(16).uppercase()}"
        return if (isImmediate) {
            v
        } else {
            "($v)"
        }
    }
}