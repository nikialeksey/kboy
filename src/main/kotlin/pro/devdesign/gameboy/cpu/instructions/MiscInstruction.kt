package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class MiscInstruction : Instruction {
    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    ) {
        when (meta.opcode()) {
            // Misc / control instructions
            0x00 -> {
                // no-op
            }
            0x10 -> {
                // stop?
            }
            0x27 -> { // decimal adjust to BCD
                var result = registers.a().get()
                if (registers.flag().n().isEnabled()) {
                    if (registers.flag().h().isEnabled()) {
                        result = (result - 0x06).and(0xFF)
                    }
                    if (registers.flag().c().isEnabled()) {
                        result = (result - 0x60).and(0xFF)
                    }
                } else {
                    if (registers.flag().h().isEnabled() || (result.and(0x0F)) > 9) {
                        result += 0x06
                    }
                    if (registers.flag().c().isEnabled() || result > 0x9F) {
                        result += 0x60
                    }
                }
                registers.flag().h().disable()
                registers.flag().z().setEnabled(result == 0)
                registers.flag().c().setEnabled(result > 0xFF)
                registers.a().set(result.and(0xFF))
            }
            0x2F -> {
                registers.flag().n().enable()
                registers.flag().h().enable()
                val value = registers.a().get()
                registers.a().set(value.inv().and(0xFF))
            }
            0x3F -> {
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(!registers.flag().c().isEnabled())
            }
            0x37 -> {
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().enable()
            }
            0x76 -> {
                // halt?
            }
            0xF3 -> {
                // disable interrupts with delay
            }
            0xFB -> {
                // enable interrupts with delay
            }
        }
    }
}