package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.operands.Operand
import com.alexeycode.kboy.cpu.interrupts.Interrupts
import com.alexeycode.kboy.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.cpu.registers.Registers
import com.alexeycode.kboy.mem.Memory

class ReturnsInstruction(
    private val registers: Registers,
    private val memory: Memory,
    private val interrupts: Interrupts
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // Returns
            0xC9 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                registers.pc().set(high.shl(8) + low)

                meta.cycles().action()
            }
            0xC0, 0xC8, 0xD0, 0xD8 -> {
                if (operands[0].check(registers)) {
                    val low = memory.read8(registers.sp().get())
                    val high = memory.read8(registers.sp().get() + 1)
                    registers.sp().set(registers.sp().get() + 2)
                    registers.pc().set(high.shl(8) + low)

                    meta.cycles().action()
                } else {
                    meta.cycles().none()
                }
            }
            0xD9 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                registers.pc().set(high.shl(8) + low)
                interrupts.enable()

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}