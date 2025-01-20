package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class CallsInstruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // Calls
            0xCD -> {
                val pc = registers.pc().get()
                memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)
                registers.pc().set(operands[0].read16(memory, registers))

                meta.cycles().action()
            }
            0xC4, 0xCC, 0xD4, 0xDC -> {
                if (operands[0].check(registers)) {
                    val pc = registers.pc().get()

                    memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                    memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                    registers.sp().set(registers.sp().get() - 2)
                    registers.pc().set(operands[1].read16(memory, registers))

                    meta.cycles().action()
                } else {
                    meta.cycles().none()
                }
            }
            else -> {
                0
            }
        }
    }
}