package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.address.Address
import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta

data class ReadInstruction(
    val nextAddress: Address,
    val isExtInstruction: Boolean,
    val instructionMeta: InstructionMeta,
    val operands: List<Operand>
)