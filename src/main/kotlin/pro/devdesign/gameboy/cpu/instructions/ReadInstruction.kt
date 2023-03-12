package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta

data class ReadInstruction(
    val nextAddress: Int,
    val isExtInstruction: Boolean,
    val instructionMeta: InstructionMeta,
    val operands: List<Operand>
)