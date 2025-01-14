package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.operands.Operand
import com.alexeycode.kboy.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.cpu.registers.Registers
import com.alexeycode.kboy.mem.Memory

class RestartsInstruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // Restarts
            0xC7, 0xCF, 0xD7, 0xDF, 0xE7, 0xEF, 0xF7, 0xFF -> {
                val pc = registers.pc().get()
                memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)
                registers.pc().set(operands[0].read16(memory, registers))

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}