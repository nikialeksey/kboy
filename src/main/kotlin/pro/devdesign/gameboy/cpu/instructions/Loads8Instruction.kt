package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class Loads8Instruction : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    ) {
        when (meta.opcode()) {
            // 8-bit loads
            0x02, 0x12, 0x22, 0x32,
            0x0A, 0x1A, 0x2A, 0x3A,
            0x06, 0x0E, 0x16, 0x1E,
            0x26, 0x2E, 0x36, 0x3E,
            in 0x40..0x75, in 0x77..0x7F,
            0xE0, 0xF0, 0xE2, 0xF2, 0xEA, 0xFA -> {
                val result = operands[1].read8(memory, registers)
                operands[0].write8(memory, registers, result.and(0xFF))
                operands[0].incOrDec(registers)
                operands[1].incOrDec(registers)
            }
        }
    }
}