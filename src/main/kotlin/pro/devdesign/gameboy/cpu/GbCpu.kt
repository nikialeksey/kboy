package pro.devdesign.gameboy.cpu

import pro.devdesign.gameboy.cpu.instructions.ExtInstruction
import pro.devdesign.gameboy.cpu.instructions.Instruction
import pro.devdesign.gameboy.cpu.instructions.Instructions
import pro.devdesign.gameboy.cpu.instructions.SimpleInstruction
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class GbCpu(
    private val registers: Registers,
    private val memory: Memory,
    private val instructions: Instructions,
    private val instruction: Instruction = SimpleInstruction(),
    private val extInstruction: Instruction = ExtInstruction()
) : Cpu {
    override fun executeNext(count: Int) {
        for (i in 0 until count) {
            val oldPc = registers.pc().get()
            val (nextAddress, isExtInstruction, instructionMeta, operands) = instructions.instruction(
                oldPc
            )
            if (isExtInstruction) {
                extInstruction.execute(
                    instructionMeta,
                    operands,
                    memory,
                    registers
                )
            } else {
                instruction.execute(
                    instructionMeta,
                    operands,
                    memory,
                    registers
                )
            }

            val newPc = registers.pc().get()
            if (newPc == oldPc) { // instruction did not touch PC
                registers.pc().set(nextAddress)
            }
        }
    }
}