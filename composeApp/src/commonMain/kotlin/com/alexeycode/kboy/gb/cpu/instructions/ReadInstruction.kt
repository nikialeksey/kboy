package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.address.Address
import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta

data class ReadInstruction(
    val nextAddress: Address,
    val isExtInstruction: Boolean,
    val instructionMeta: InstructionMeta,
    val operands: List<Operand>
)