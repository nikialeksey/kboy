package pro.devdesign.gameboy.cpu

import pro.devdesign.gameboy.cpu.instructions.ExtInstruction
import pro.devdesign.gameboy.cpu.instructions.Instruction
import pro.devdesign.gameboy.cpu.instructions.Instructions
import pro.devdesign.gameboy.cpu.instructions.SimpleInstruction
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.cpu.timer.Timer
import pro.devdesign.gameboy.mem.Memory

class GbCpu(
    private val registers: Registers,
    private val memory: Memory,
    private val interrupts: Interrupts,
    private val timer: Timer,
    private val instructions: Instructions,
    private val instruction: Instruction = SimpleInstruction(registers, memory, interrupts),
    private val extInstruction: Instruction = ExtInstruction(registers, memory)
) : Cpu {

    private var haltMode = false

    override fun execute(cpuCycles: Int) {
        for (i in 0 until cpuCycles) {
            if (!haltMode) {
                runInterrupts()

                val oldPc = registers.pc().get()
                val instructionData = instructions.instruction(oldPc)
                registers.pc().set(instructionData.nextAddress.asInt())

                val isExt = instructionData.isExtInstruction
                val clockCyclesSpent = if (isExt) {
                    extInstruction.execute(
                        instructionData.instructionMeta,
                        instructionData.operands
                    )
                } else {
                    instruction.execute(
                        instructionData.instructionMeta,
                        instructionData.operands
                    )
                }
                timer.tick(clockCyclesSpent)

                val opcode = instructionData.instructionMeta.opcode()
                if (!isExt && opcode == 0x76) {
                    haltMode = true
                }
            } else {
                // halt mode enabled
                timer.tick(4)

                if (interrupts.ieFlag().and(interrupts.ifFlag()) != 0) {
                    haltMode = false
                }
            }
        }
    }

    private fun runInterrupts() {
        interrupts.tryRun { address: Int ->
            val pc = registers.pc().get()
            memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
            memory.write8(registers.sp().get() - 2, pc.and(0xFF))
            registers.sp().set(registers.sp().get() - 2)
            registers.pc().set(address)

            // TODO should I do timer.tick() here?
            timer.tick(4)
        }
    }
}