package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.cpu.timer.Timer
import pro.devdesign.gameboy.mem.Memory

class SimpleInstruction : Instruction {

    private val instructions: List<Instruction>
    private val registers: Registers
    private val memory: Memory

    constructor(
        registers: Registers,
        memory: Memory,
        timer: Timer,
        interrupts: Interrupts
    ) : this(
        listOf(
            MiscInstruction(registers, memory, timer, interrupts),
            JumpsInstruction(registers, memory, timer),
            CallsInstruction(registers, memory, timer),
            RestartsInstruction(registers, memory, timer),
            ReturnsInstruction(registers, memory, timer, interrupts),
            Loads8Instruction(registers, memory, timer),
            Loads16Instruction(registers, memory, timer),
            Alu8Instruction(registers, memory, timer),
            Alu16Instruction(registers, memory, timer),
            SimpleRotateInstruction(registers, memory, timer)
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
    ) {
        try {
            for (instruction in instructions) {
                instruction.execute(meta, operands)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}