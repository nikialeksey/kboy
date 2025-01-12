package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.address.Address
import com.alexeycode.kboy.cpu.instructions.operands.Operand
import com.alexeycode.kboy.cpu.opcodes.InstructionMeta

data class ReadInstruction(
    val nextAddress: Address,
    val isExtInstruction: Boolean,
    val instructionMeta: InstructionMeta,
    val operands: List<Operand>
)