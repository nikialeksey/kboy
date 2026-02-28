package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class SimpleInstruction : Instruction {

    private val instructions: List<Instruction>
    private val registers: Registers

    constructor(
        registers: Registers,
        memory: Memory,
        interrupts: Interrupts
    ) : this(
        listOf(
            MiscInstruction(registers, memory, interrupts),
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
    )

    constructor(
        instructions: List<Instruction>,
        registers: Registers,
    ) {
        this.instructions = instructions
        this.registers = registers
    }

    override fun execute(opcode: Int): Int {
        try {
            for (i in 0 until instructions.size) {
                val instruction = instructions[i]
                val clockCycles = instruction.execute(opcode)
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