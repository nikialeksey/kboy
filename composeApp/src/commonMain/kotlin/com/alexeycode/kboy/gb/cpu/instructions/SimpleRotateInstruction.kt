package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers

class SimpleRotateInstruction(
    private val r: Registers,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 8-bit shift, rotate and bit instructions
            0x07 -> {
                val n = r.a().get()
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                r.a().set((n.shl(1) + c).and(0xFF))

                r.flag().z().disable()
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                4
            }
            0x17 -> {
                val n = r.a().get()
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val prevC = if (r.flag().c().isEnabled()) 1 else 0
                r.a().set((n.shl(1) + prevC).and(0xFF))

                r.flag().z().disable()
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                4
            }
            0x0F -> {
                val n = r.a().get()
                val c = n.and(0b0000_0001)
                r.a().set((n.and(0xFF).shr(1) + c.shl(7)).and(0xFF))

                r.flag().z().disable()
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                4
            }
            0x1F -> {
                val n = r.a().get()
                val c = n.and(0b0000_0001)
                val prevC = if (r.flag().c().isEnabled()) 1 else 0
                r.a().set((n.and(0xFF).shr(1) + prevC.shl(7)).and(0xFF))

                r.flag().z().disable()
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(c == 1)

                4
            }
            else -> {
                0
            }
        }
    }
}