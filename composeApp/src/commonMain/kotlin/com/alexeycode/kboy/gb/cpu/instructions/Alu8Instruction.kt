package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Alu8Instruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        val opcode = meta.opcode()
        return when (opcode) {
            // 8-bit arithmetic / logical instructions
            // 8-bit increments
            0x04, 0x0C, 0x14, 0x1C, 0x24, 0x2C, 0x34, 0x3C -> {
                val value = operands[0].read8(memory, registers)
                val result = (value + 1).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((value.and(0x0F) + 1.and(0x0F)) > 0x0F)

                meta.cycles().action()
            }
            // 8-bit decrements
            0x05, 0x0D, 0x15, 0x1D, 0x25, 0x2D, 0x35, 0x3D -> {
                val value = operands[0].read8(memory, registers)
                val result = (value - 1).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((value.and(0x0F) - 1.and(0x0F)) < 0)

                meta.cycles().action()
            }
            // 8-bit add
            in 0x80..0x87, 0xC6 -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a + n).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)

                meta.cycles().action()
            }
            // 8-bit adc
            in 0x88..0x8F, 0xCE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val c = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (a + n + c).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F) + c) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF) + c) > 0xFF)

                meta.cycles().action()
            }
            // 8-bit sub
            in 0x90..0x97, 0xD6 -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a - n).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)

                meta.cycles().action()
            }
            // 8-bit sbc
            in 0x98..0x9F, 0xDE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val c = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (a - n - c).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F) - c) < 0)
                // TODO interesting, but here C flag is not always updated:
                // https://github.com/gbdev/gb-opcodes/commit/d091dd1d4a4b9fe783b53d19a8b0c50a28c0c92c
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF) - c) < 0)

                meta.cycles().action()
            }
            // 8-bit and
            in 0xA0..0xA7, 0xE6 -> {
                val a = registers.a().get()
                val n = operands[1].read8(memory, registers)
                val result = a.and(n)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().enable()
                registers.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit xor
            in 0xA8..0xAF, 0xEE -> {
                val a = registers.a().get()
                val n = operands[1].read8(memory, registers)
                val result = a.xor(n)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit or
            in 0xB0..0xB7, 0xF6 -> {
                val a = registers.a().get()
                val n = operands[1].read8(memory, registers)
                val result = a.or(n)
                registers.a().set(result)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit cp
            in 0xB8..0xBF, 0xFE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a - n).and(0xFF)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)

                meta.cycles().action()
            }

            else -> {
                0
            }
        }
    }
}