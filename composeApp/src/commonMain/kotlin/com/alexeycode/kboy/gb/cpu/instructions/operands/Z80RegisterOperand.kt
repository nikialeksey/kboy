package com.alexeycode.kboy.gb.cpu.instructions.operands

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Z80RegisterOperand : Operand {
    private val name: String
    private val sign: Int
    private val isImmediate: Boolean

    constructor(
        name: String,
        sign: Int,
        isImmediate: Boolean
    ) {
        this.name = name
        this.sign = sign
        this.isImmediate = isImmediate
    }

    override fun read8(memory: Memory, registers: Registers): Int {
        val register = registers.byName(name)
        val value = register.get()
        return if (isImmediate) {
            value
        } else {
            memory.read8(value)
        }
    }

    override fun write8(memory: Memory, registers: Registers, value: Int) {
        val register = registers.byName(name)
        if (isImmediate) {
            register.set(value)
        } else {
            if (register.bytes() == 1) {
                memory.write8(0xFF00 + register.get(), value)
            } else {
                memory.write8(register.get(), value)
            }
        }
    }

    override fun read16(memory: Memory, registers: Registers): Int {
        val register = registers.byName(name)
        return if (isImmediate) {
            register.get()
        } else {
            throw IllegalStateException(
                "You can not read word from register not immediate operands!"
            )
        }
    }

    override fun write16(memory: Memory, registers: Registers, value: Int) {
        val register = registers.byName(name)
        if (isImmediate) {
            register.set(value)
        } else {
            val address = register.get()
            memory.write8(address, value.and(0xFF))
            memory.write8(address + 1, value.shr(8).and(0xFF))
        }
    }

    override fun incOrDec(registers: Registers) {
        val register = registers.byName(name)
        if (sign > 0) {
            register.set(register.get() + 1)
        } else if (sign < 0) {
            register.set(register.get() - 1)
        }
    }

    override fun toString(): String {
        val v = name
        val vSign = if (sign == 0) {
            v
        } else if (sign < 0) {
            "$v-"
        } else {
            "$v+"
        }
        return if (isImmediate) {
            vSign
        } else {
            "($vSign)"
        }
    }
}