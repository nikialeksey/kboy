package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class SimpleInstruction : Instruction {

    private val instructions: List<Instruction>

    constructor() : this(
        MiscInstruction(),
        JumpsInstruction(),
        CallsInstruction(),
        RestartsInstruction(),
        ReturnsInstruction(),
        Loads8Instruction(),
        Loads16Instruction(),
        Alu8Instruction(),
        Alu16Instruction(),
        SimpleRotateInstruction()
    )

    constructor(vararg instruction: Instruction) : this(instruction.toList())

    constructor(instructions: List<Instruction>) {
        this.instructions = instructions
    }

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    ) {
        try {
            for (instruction in instructions) {
                instruction.execute(meta, operands, memory, registers)
            }
        } catch (e: Exception) {

        }
    }
}