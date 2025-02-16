package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class ReturnsInstruction(
    private val r: Registers,
    private val mem: Memory,
    private val interrupts: Interrupts
) : Instruction {

    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        return when (opcode) {
            // Returns
            0xC9 -> {
                val low = mem.read8(r.sp().get())
                val high = mem.read8(r.sp().get() + 1)
                r.sp().set(r.sp().get() + 2)
                r.pc().set(high.shl(8) + low)

                16
            }
            0xC0 -> conditionalRet({ !r.flag().z().isEnabled() })
            0xC8 -> conditionalRet({ r.flag().z().isEnabled() })
            0xD0 -> conditionalRet({ !r.flag().c().isEnabled() })
            0xD8 -> conditionalRet({ r.flag().c().isEnabled() })
            0xD9 -> {
                val low = mem.read8(r.sp().get())
                val high = mem.read8(r.sp().get() + 1)
                r.sp().set(r.sp().get() + 2)
                r.pc().set(high.shl(8) + low)
                interrupts.enable()

                16
            }
            else -> {
                0
            }
        }
    }

    private fun conditionalRet(condition: () -> Boolean): Int {
        return if (condition()) {
            val low = mem.read8(r.sp().get())
            val high = mem.read8(r.sp().get() + 1)
            r.sp().set(r.sp().get() + 2)
            r.pc().set(high.shl(8) + low)

            20
        } else {
            8
        }
    }
}