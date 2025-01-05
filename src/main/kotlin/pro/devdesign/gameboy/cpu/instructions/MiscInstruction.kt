package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.cpu.timer.Timer
import pro.devdesign.gameboy.mem.Memory

class MiscInstruction(
    private val registers: Registers,
    private val memory: Memory,
    private val timer: Timer,
    private val interrupts: Interrupts
) : Instruction {
    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
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
                if (result > 0xFF || result < 0) {
                    registers.flag().c().enable()
                }
                result = result.and(0xFF)
                registers.flag().z().setEnabled(result == 0)
                registers.a().set(result)

                timer.tick(meta.cycles().action())
            }
            0x2F -> {
                registers.flag().n().enable()
                registers.flag().h().enable()
                val value = registers.a().get()
                registers.a().set(value.inv().and(0xFF))

                timer.tick(meta.cycles().action())
            }
            0x3F -> {
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().setEnabled(!registers.flag().c().isEnabled())

                timer.tick(meta.cycles().action())
            }
            0x37 -> {
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().enable()

                timer.tick(meta.cycles().action())
            }
            0x76 -> {
                // halt?

                timer.tick(meta.cycles().action())
            }
            0xF3 -> {
                // disable interrupts
                interrupts.disable()

                timer.tick(meta.cycles().action())
            }
            0xFB -> {
                // enable interrupts
                interrupts.enable()

                timer.tick(meta.cycles().action())
            }
        }
    }
}