package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class SimpleInstruction : Instruction {

    private val instructions: List<Instruction>
    private val registers: Registers
    private val memory: Memory

    constructor(
        registers: Registers,
        memory: Memory,
        interrupts: Interrupts
    ) : this(
        listOf(
            MiscInstruction(registers, interrupts),
            JumpsInstruction(registers, memory),
            CallsInstruction(registers, memory),
            RestartsInstruction(registers, memory),
            ReturnsInstruction(registers, memory, interrupts),
            Loads8Instruction(registers, memory),
            Loads16Instruction(registers, memory),
            Alu8Instruction(registers, memory),
            Alu16Instruction(registers, memory),
            SimpleRotateInstruction(registers)
        ),
        registers,
        memory
    )

    constructor(
        instructions: List<Instruction>,
        registers: Registers,
        memory: Memory
    ) {
        this.instructions = instructions
        this.registers = registers
        this.memory = memory
    }

    override fun execute(
        opcode: Int,
        operands: List<Operand>
    ): Int {
        try {
            for (instruction in instructions) {
                val clockCycles = instruction.execute(opcode, operands)
                if (clockCycles != 0) {
                    return clockCycles
                }
            }
        } catch (e: Exception) {
            throw e
        }
        throw IllegalArgumentException("Unknown instruction! Instruction opcode: ${opcode.toString(16)}")
    }
}