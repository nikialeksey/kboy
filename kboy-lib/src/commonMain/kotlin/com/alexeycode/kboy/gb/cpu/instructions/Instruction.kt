package com.alexeycode.kboy.gb.cpu.instructions

interface Instruction {
    /**
     * Execute instruction with operands
     *
     * @param meta instruction info
     *
     * @return number of Clock cycles spent
     */
    fun execute(opcode: Int): Int
}