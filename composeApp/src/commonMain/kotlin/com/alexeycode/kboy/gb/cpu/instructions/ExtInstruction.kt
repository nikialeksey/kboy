package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class ExtInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {
    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        return when (opcode) {
            // 8-bit shift, rotate and bit instructions
            in 0x00..0x07 -> {
                val n = operands[0].read8(mem, r)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val result = (n.shl(1) + c).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x06) {
                    16
                } else {
                    8
                }
            }
            in 0x08..0x0F -> {
                val n = operands[0].read8(mem, r)
                val c = n.and(0b0000_0001)
                val result = (n.and(0xFF).shr(1) + c.shl(7)).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x0E) {
                    16
                } else {
                    8
                }
            }
            in 0x10..0x17 -> {
                val n = operands[0].read8(mem, r)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val prevC = if (r.flag().c().isEnabled()) 1 else 0
                val result = (n.shl(1) + prevC).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x16) {
                    16
                } else {
                    8
                }
            }
            in 0x18..0x1F -> {
                val n = operands[0].read8(mem, r)
                val c = n.and(0b0000_0001)
                val prevC = if (r.flag().c().isEnabled()) 1 else 0
                val result = (n.and(0xFF).shr(1) + prevC.shl(7)).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x1E) {
                    16
                } else {
                    8
                }
            }
            in 0x20..0x27 -> {
                val n = operands[0].read8(mem, r)
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val result = n.shl(1).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x26) {
                    16
                } else {
                    8
                }
            }
            in 0x28..0x2F -> {
                val n = operands[0].read8(mem, r)
                val c = n.and(0b0000_0001)
                val result = n.and(0xFF).shr(1).and(0xFF) + n.and(0b1000_0000)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x2E) {
                    16
                } else {
                    8
                }
            }
            in 0x30..0x37 -> {
                val n = operands[0].read8(mem, r)
                val result = n.and(0x0F).shl(4) + n.and(0xF0).shr(4)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().disable()

                if (opcode == 0x36) {
                    16
                } else {
                    8
                }
            }
            in 0x38..0x3F -> {
                val n = operands[0].read8(mem, r)
                val c = n.and(0b0000_0001)
                val result = n.and(0xFF).shr(1).and(0xFF)
                operands[0].write8(mem, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                if (opcode == 0x3E) {
                    16
                } else {
                    8
                }
            }
            in 0x40..0x7F -> {
                val n = operands[0].read8(mem, r)
                val a = operands[1].read8(mem, r)
                val result = a.and(1.shl(n)) != 0

                r.flag().z().setEnabled(!result)
                r.flag().n().disable()
                r.flag().h().enable()

                if (opcode == 0x46 || opcode == 0x56 || opcode == 0x66 || opcode == 0x76) {
                    12
                } else if (opcode == 0x4E || opcode == 0x5E || opcode == 0x6E || opcode == 0x7E) {
                    12
                } else {
                    8
                }
            }
            in 0x80..0xBF -> {
                val n = operands[0].read8(mem, r)
                val a = operands[1].read8(mem, r)
                operands[1].write8(mem, r, a.and(1.shl(n).inv()).and(0xFF))

                if (opcode == 0x86 || opcode == 0x96 || opcode == 0xA6 || opcode == 0xB6) {
                    16
                } else if (opcode == 0x8E || opcode == 0x9E || opcode == 0xAE || opcode == 0xBE) {
                    16
                } else {
                    8
                }
            }
            in 0xC0..0xFF -> {
                val n = operands[0].read8(mem, r)
                val a = operands[1].read8(mem, r)
                operands[1].write8(mem, r, a.or(1.shl(n)).and(0xFF))

                if (opcode == 0xC6 || opcode == 0xD6 || opcode == 0xE6 || opcode == 0xF6) {
                    16
                } else if (opcode == 0xCE || opcode == 0xDE || opcode == 0xEE || opcode == 0xFE) {
                    16
                } else {
                    8
                }
            }
            else -> {
                0
            }
        }
    }
}