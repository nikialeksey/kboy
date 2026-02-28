package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNext16
import com.alexeycode.kboy.gb.mem.readNextSigned8

class JumpsInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // Jumps
            0x18 -> jumpStep({ mem.readNextSigned8(r) /* e8 */ })
            0x20 -> conditionalJumpStep({ !r.flag().z().isEnabled() }, { mem.readNextSigned8(r) /* e8 */ })
            0x28 -> conditionalJumpStep({ r.flag().z().isEnabled() }, { mem.readNextSigned8(r) /* e8 */ })
            0x30 -> conditionalJumpStep({ !r.flag().c().isEnabled() }, { mem.readNextSigned8(r) /* e8 */ })
            0x38 -> conditionalJumpStep({ r.flag().c().isEnabled() }, { mem.readNextSigned8(r) /* e8 */ })
            0xC3 -> {
                r.pc().set(mem.readNext16(r) /* a16 */)
                16
            }
            0xC2 -> conditionalJumpTo({ !r.flag().z().isEnabled() }, { mem.readNext16(r) /* a16 */ })
            0xCA -> conditionalJumpTo({ r.flag().z().isEnabled() }, { mem.readNext16(r) /* a16 */ })
            0xD2 -> conditionalJumpTo({ !r.flag().c().isEnabled() }, { mem.readNext16(r) /* a16 */ })
            0xDA -> conditionalJumpTo({ r.flag().c().isEnabled() }, { mem.readNext16(r) /* a16 */ })
            0xE9 -> {
                r.pc().set(r.hl().get())
                4
            }
            else -> {
                0
            }
        }
    }

    private inline fun jumpStep(step: () -> Int): Int {
        val s = step()
        r.pc().set(r.pc().get() + s)

        return 12
    }

    private inline fun conditionalJumpStep(condition: () -> Boolean, step: () -> Int): Int {
        val s = step()
        return if (condition()) {
            r.pc().set(r.pc().get() + s)

            12
        } else {
            8
        }
    }

    private inline fun conditionalJumpTo(condition: () -> Boolean, to: () -> Int): Int {
        val t = to()
        return if (condition()) {
            r.pc().set(t)

            16
        } else {
            12
        }
    }
}