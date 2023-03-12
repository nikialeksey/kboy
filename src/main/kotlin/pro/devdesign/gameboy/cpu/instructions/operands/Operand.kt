package pro.devdesign.gameboy.cpu.instructions.operands

import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

interface Operand {
    fun read8(memory: Memory, registers: Registers): Int
    fun write8(memory: Memory, registers: Registers, value: Int)
    fun read16(memory: Memory, registers: Registers): Int
    fun write16(memory: Memory, registers: Registers, value: Int)
    fun incOrDec(registers: Registers)
    fun check(registers: Registers): Boolean
}