package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers

class MiscInstruction(
    private val r: Registers,
    private val interrupts: Interrupts
) : Instruction {
    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        return when (opcode) {
            // Misc / control instructions
            0x00 -> {
                // no-op

                4
            }
            0x10 -> {
                // stop? TODO proceed n8 here
                // https://gist.github.com/SonoSooS/c0055300670d678b5ae8433e20bea595#nop-and-stop
                operands[0]

                4
            }
            0x27 -> { // decimal adjust to BCD
                var result = r.a().get()
                if (r.flag().n().isEnabled()) {
                    if (r.flag().h().isEnabled()) {
                        result = (result - 0x06).and(0xFF)
                    }
                    if (r.flag().c().isEnabled()) {
                        result = (result - 0x60).and(0xFF)
                    }
                } else {
                    if (r.flag().h().isEnabled() || (result.and(0x0F)) > 9) {
                        result += 0x06
                    }
                    if (r.flag().c().isEnabled() || result > 0x9F) {
                        result += 0x60
                    }
                }

                r.flag().h().disable()
                if (result > 0xFF || result < 0) {
                    r.flag().c().enable()
                }
                result = result.and(0xFF)
                r.flag().z().setEnabled(result == 0)
                r.a().set(result)

                4
            }
            0x37 -> {
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().enable()

                4
            }
            0x2F -> {
                r.flag().n().enable()
                r.flag().h().enable()
                val value = r.a().get()
                r.a().set(value.inv().and(0xFF))

                4
            }
            0x3F -> {
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().setEnabled(!r.flag().c().isEnabled())

                4
            }
            0x76 -> {
                // halt?

                4
            }
            0xF3 -> {
                // disable interrupts
                interrupts.disable()

                4
            }
            0xFB -> {
                // enable interrupts
                interrupts.enable()

                4
            }
            else -> {
                0
            }
        }
    }
}