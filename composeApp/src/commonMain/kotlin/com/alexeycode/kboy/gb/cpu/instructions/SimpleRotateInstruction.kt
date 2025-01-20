package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers

class SimpleRotateInstruction(
    private val registers: Registers,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 8-bit shift, rotate and bit instructions
            0x07 -> {
                val n = registers.a().get()
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                registers.a().set((n.shl(1) + c).and(0xFF))

                registers.flag().z().disable()
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            0x17 -> {
                val n = registers.a().get()
                val c = if (n.and(0b1000_0000) == 0) 0 else 1
                val prevC = if (registers.flag().c().isEnabled()) 1 else 0
                registers.a().set((n.shl(1) + prevC).and(0xFF))

                registers.flag().z().disable()
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            0x0F -> {
                val n = registers.a().get()
                val c = n.and(0b0000_0001)
                registers.a().set((n.and(0xFF).shr(1) + c.shl(7)).and(0xFF))

                registers.flag().z().disable()
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            0x1F -> {
                val n = registers.a().get()
                val c = n.and(0b0000_0001)
                val prevC = if (registers.flag().c().isEnabled()) 1 else 0
                registers.a().set((n.and(0xFF).shr(1) + prevC.shl(7)).and(0xFF))

                registers.flag().z().disable()
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(c == 1)

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}