package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta

interface Instruction {
    fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    )
}