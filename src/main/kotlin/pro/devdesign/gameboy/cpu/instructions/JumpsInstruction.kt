package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class JumpsInstruction(
    private val registers: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // Jumps
            0x18 -> {
                val step = operands[0].read8(memory, registers)
                registers.pc().set(registers.pc().get() + step)

                meta.cycles().action()
            }
            0x20, 0x28, 0x30, 0x38 -> {
                if (operands[0].check(registers)) {

                    val step = operands[1].read8(memory, registers)
                    registers.pc().set(registers.pc().get() + step)

                    meta.cycles().action()
                } else {
                    meta.cycles().none()
                }
            }
            0xC3 -> {
                registers.pc().set(operands[0].read16(memory, registers))

                meta.cycles().action()
            }
            0xC2, 0xCA, 0xD2, 0xDA -> {
                if (operands[0].check(registers)) {
                    registers.pc().set(operands[1].read16(memory, registers))

                    meta.cycles().action()
                } else {
                    meta.cycles().none()
                }
            }
            0xE9 -> {
                registers.pc().set(operands[0].read16(memory, registers))

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}