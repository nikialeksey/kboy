package pro.devdesign.gameboy.cpu

import pro.devdesign.gameboy.cpu.instructions.ExtInstruction
import pro.devdesign.gameboy.cpu.instructions.Instruction
import pro.devdesign.gameboy.cpu.instructions.Instructions
import pro.devdesign.gameboy.cpu.instructions.SimpleInstruction
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class GbCpu(
    private val registers: Registers,
    private val memory: Memory,
    private val interrupts: Interrupts,
    private val instructions: Instructions,
    private val instruction: Instruction = SimpleInstruction(registers, memory, interrupts),
    private val extInstruction: Instruction = ExtInstruction(registers, memory)
) : Cpu {
    override fun executeNext(count: Int) {
        for (i in 0 until count) {
            val oldPc = registers.pc().get()
            val instructionData = instructions.instruction(oldPc)
            registers.pc().set(instructionData.nextAddress.asInt())

            if (instructionData.isExtInstruction) {
                extInstruction.execute(instructionData.instructionMeta, instructionData.operands)
            } else {
                instruction.execute(instructionData.instructionMeta, instructionData.operands)
            }
        }
    }
}