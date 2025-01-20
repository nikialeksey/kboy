package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class ExtInstruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {
    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 8-bit shift, rotate and bit instructions
            in 0x00..0x07 -> {
                val n = operands[0].read8(memory, registers)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val result = (n.shl(1) + c).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x08..0x0F -> {
                val n = operands[0].read8(memory, registers)
                val c = n.and(0b0000_0001)
                val result = (n.and(0xFF).shr(1) + c.shl(7)).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x10..0x17 -> {
                val n = operands[0].read8(memory, registers)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val prevC = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (n.shl(1) + prevC).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x18..0x1F -> {
                val n = operands[0].read8(memory, registers)
                val c = n.and(0b0000_0001)
                val prevC = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (n.and(0xFF).shr(1) + prevC.shl(7)).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x20..0x27 -> {
                val n = operands[0].read8(memory, registers)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val result = n.shl(1).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x28..0x2F -> {
                val n = operands[0].read8(memory, registers)
                val c = n.and(0b0000_0001)
                val result = n.and(0xFF).shr(1).and(0xFF) + n.and(0b1000_0000)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x30..0x37 -> {
                val n = operands[0].read8(memory, registers)
                val result = n.and(0x0F).shl(4) + n.and(0xF0).shr(4)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().disable()

                meta.cycles().action()
            }
            in 0x38..0x3F -> {
                val n = operands[0].read8(memory, registers)
                val c = n.and(0b0000_0001)
                val result = n.and(0xFF).shr(1).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            in 0x40..0x7F -> {
                val n = operands[0].read8(memory, registers)
                val a = operands[1].read8(memory, registers)
                val result = a.and(1.shl(n)) != 0

                registers.flag().z().setEnabled(!result)
                registers.flag().n().disable()
                registers.flag().h().enable()

                meta.cycles().action()
            }
            in 0x80..0xBF -> {
                val n = operands[0].read8(memory, registers)
                val a = operands[1].read8(memory, registers)
                operands[1].write8(memory, registers, a.and(1.shl(n).inv()).and(0xFF))

                meta.cycles().action()
            }
            in 0xC0..0xFF -> {
                val n = operands[0].read8(memory, registers)
                val a = operands[1].read8(memory, registers)
                operands[1].write8(memory, registers, a.or(1.shl(n)).and(0xFF))

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}