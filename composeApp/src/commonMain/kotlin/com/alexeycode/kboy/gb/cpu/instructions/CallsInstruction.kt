package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class CallsInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        return when (opcode) {
            // Calls
            0xCD -> {
                call({ operands[0].read16(mem, r) /* a16 */ })
            }
            0xC4 -> conditionCall({ !r.flag().z().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xCC -> conditionCall({ r.flag().z().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xD4 -> conditionCall({ !r.flag().c().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xDC -> conditionCall({ r.flag().c().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })

            else -> {
                0
            }
        }
    }

    private fun conditionCall(condition: () -> Boolean, nextAddress: () -> Int): Int {
        return if (condition()) {
            val pc = r.pc().get()

            mem.write8(r.sp().get() - 1, pc.shr(8).and(0xFF))
            mem.write8(r.sp().get() - 2, pc.and(0xFF))
            r.sp().set(r.sp().get() - 2)
            r.pc().set(nextAddress())

            24
        } else {
            12
        }
    }

    private fun call(nextAddress: () -> Int): Int {
        val pc = r.pc().get()
        mem.write8(r.sp().get() - 1, pc.shr(8).and(0xFF))
        mem.write8(r.sp().get() - 2, pc.and(0xFF))
        r.sp().set(r.sp().get() - 2)
        r.pc().set(nextAddress())

        return 24
    }
}