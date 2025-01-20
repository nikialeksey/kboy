package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Alu16Instruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 16-bit arithmetic / logical instructions
            // 16-bit inc
            0x03, 0x13, 0x23, 0x33 -> {
                val value = operands[0].read16(memory, registers)
                val result = (value + 1).and(0xFFFF)
                operands[0].write16(memory, registers, result)

                meta.cycles().action()
            }
            // 16-bit dec
            0x0B, 0x1B, 0x2B, 0x3B -> {
                val value = operands[0].read16(memory, registers)
                val result = (value - 1).and(0xFFFF)
                operands[0].write16(memory, registers, result)

                meta.cycles().action()
            }
            // 16-bit add
            0x09, 0x19, 0x29, 0x39 -> {
                val a = operands[0].read16(memory, registers)
                val n = operands[1].read16(memory, registers)
                val result = (a + n).and(0xFFFF)
                operands[0].write16(memory, registers, result)

                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0FFF) + n.and(0x0FFF)) > 0x0FFF)
                registers.flag().c().setEnabled((a + n) > 0xFFFF)

                meta.cycles().action()
            }
            0xE8 -> {
                val a = operands[0].read16(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a + n).and(0xFFFF)
                operands[0].write16(memory, registers, result)

                registers.flag().z().disable()
                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}