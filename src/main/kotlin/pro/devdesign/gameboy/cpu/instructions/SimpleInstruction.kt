package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

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
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        try {
            for (instruction in instructions) {
                val clockCycles = instruction.execute(meta, operands)
                if (clockCycles != 0) {
                    return clockCycles
                }
            }
        } catch (e: Exception) {
            throw e
        }
        throw IllegalArgumentException("Unknown instruction! Instruction meta: $meta")
    }
}