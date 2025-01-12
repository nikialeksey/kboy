package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.operands.Operand
import com.alexeycode.kboy.cpu.opcodes.InstructionMeta

interface Instruction {
    /**
     * Execute instruction with operands
     *
     * @param meta instruction info
     * @param operands instruction arguments
     *
     * @return number of Clock cycles spent
     */
    fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int
}