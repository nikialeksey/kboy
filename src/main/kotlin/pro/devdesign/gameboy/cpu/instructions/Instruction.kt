package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

interface Instruction {
    fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    )
}