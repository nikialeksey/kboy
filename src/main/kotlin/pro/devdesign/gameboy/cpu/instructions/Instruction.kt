package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta

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