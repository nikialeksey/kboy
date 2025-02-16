package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Loads16Instruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        return when (opcode) {
            // 16-bit load instructions
            0x01, 0x11, 0x21, 0x31 -> {
                val result = operands[1].read16(mem, r)
                operands[0].write16(mem, r, result)

                12
            }
            0x08 -> {
                val result = operands[1].read16(mem, r)
                operands[0].write16(mem, r, result)

                20
            }
            0xF8 -> {
                val a = operands[1].read16(mem, r)
                val b = operands[2].read8(mem, r)
                val result = a + b
                operands[0].write16(mem, r, result)

                r.flag().z().disable()
                r.flag().n().disable()
                // TODO i'm not sure that I should use here 8-bit comparison
                r.flag().h().setEnabled((a.and(0x0F) + b.and(0x0F)) > 0x0F)
                r.flag().c().setEnabled((a.and(0xFF) + b.and(0xFF)) > 0xFF)

                12
            }
            0xF9 -> {
                val result = operands[1].read16(mem, r)
                operands[0].write16(mem, r, result)

                8
            }
            0xC1, 0xD1, 0xE1, 0xF1 -> {
                val low = mem.read8(r.sp().get())
                val high = mem.read8(r.sp().get() + 1)
                r.sp().set(r.sp().get() + 2)
                operands[0].write16(mem, r, high.shl(8) + low)

                12
            }
            0xC5, 0xD5, 0xE5, 0xF5 -> {
                val value = operands[0].read16(mem, r)
                mem.write8(r.sp().get() - 1, value.shr(8).and(0xFF))
                mem.write8(r.sp().get() - 2, value.and(0xFF))
                r.sp().set(r.sp().get() - 2)

                16
            }
            else -> {
                0
            }
        }
    }
}