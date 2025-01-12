package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.operands.Operand
import com.alexeycode.kboy.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.cpu.registers.Registers
import com.alexeycode.kboy.mem.Memory

class Loads16Instruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 16-bit load instructions
            0x01, 0x11, 0x21, 0x31, 0xF9, 0x08 -> {
                val result = operands[1].read16(memory, registers)
                operands[0].write16(memory, registers, result)

                meta.cycles().action()
            }
            0xF8 -> {
                val a = operands[1].read16(memory, registers)
                val b = operands[2].read8(memory, registers)
                val result = a + b
                operands[0].write16(memory, registers, result)

                registers.flag().z().disable()
                registers.flag().n().disable()
                // TODO i'm not sure that I should use here 8-bit comparison
                registers.flag().h().setEnabled((a.and(0x0F) + b.and(0x0F)) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + b.and(0xFF)) > 0xFF)

                meta.cycles().action()
            }
            0xC1, 0xD1, 0xE1, 0xF1 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                operands[0].write16(memory, registers, high.shl(8) + low)

                meta.cycles().action()
            }
            0xC5, 0xD5, 0xE5, 0xF5 -> {
                val value = operands[0].read16(memory, registers)
                memory.write8(registers.sp().get() - 1, value.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, value.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}