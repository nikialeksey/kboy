package pro.devdesign.gameboy.cpu.instructions.operands

import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

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
        throw IllegalAccessException("You can not write to literal operand!")
    }

    override fun read16(memory: Memory, registers: Registers): Int {
        return name.substringBefore("H").toInt(16)
    }

    override fun write16(memory: Memory, registers: Registers, value: Int) {
        throw IllegalAccessException("You can not write to literal operand!")
    }

    override fun incOrDec(registers: Registers) {
        throw IllegalAccessException("You can not inc/dec to literal operand!")
    }

    override fun check(registers: Registers): Boolean {
        throw IllegalAccessException("You can not check register operands!")
    }

    override fun toString(): String {
        return name
    }
}