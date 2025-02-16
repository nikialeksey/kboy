package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class JumpsInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // Jumps
            0x18 -> jumpStep({ operands[0].read8(mem, r) /* e8 */ })
            0x20 -> conditionalJumpStep({ !r.flag().z().isEnabled() }, { operands[1].read8(mem, r) /* e8 */ })
            0x28 -> conditionalJumpStep({ r.flag().z().isEnabled() }, { operands[1].read8(mem, r) /* e8 */ })
            0x30 -> conditionalJumpStep({ !r.flag().c().isEnabled() }, { operands[1].read8(mem, r) /* e8 */ })
            0x38 -> conditionalJumpStep({ r.flag().c().isEnabled() }, { operands[1].read8(mem, r) /* e8 */ })
            0xC3 -> {
                r.pc().set(operands[0].read16(mem, r) /* a16 */)
                16
            }
            0xC2 -> conditionalJumpTo({ !r.flag().z().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xCA -> conditionalJumpTo({ r.flag().z().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xD2 -> conditionalJumpTo({ !r.flag().c().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xDA -> conditionalJumpTo({ r.flag().c().isEnabled() }, { operands[1].read16(mem, r) /* a16 */ })
            0xE9 -> {
                r.pc().set(r.hl().get())
                4
            }
            else -> {
                0
            }
        }
    }

    private fun jumpStep(step: () -> Int): Int {
        r.pc().set(r.pc().get() + step())

        return 12
    }

    private fun conditionalJumpStep(condition: () -> Boolean, step: () -> Int): Int {
        return if (condition()) {
            r.pc().set(r.pc().get() + step())

            12
        } else {
            8
        }
    }

    private fun conditionalJumpTo(condition: () -> Boolean, to: () -> Int): Int {
        return if (condition()) {
            r.pc().set(to())

            16
        } else {
            12
        }
    }
}