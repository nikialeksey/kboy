package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.interrupts.Interrupts
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.cpu.timer.Timer
import pro.devdesign.gameboy.mem.Memory

class ReturnsInstruction(
    private val registers: Registers,
    private val memory: Memory,
    private val timer: Timer,
    private val interrupts: Interrupts
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ) {
        when (meta.opcode()) {
            // Returns
            0xC9 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                registers.pc().set(high.shl(8) + low)

                timer.tick(meta.cycles().action())
            }
            0xC0, 0xC8, 0xD0, 0xD8 -> {
                if (operands[0].check(registers)) {
                    val low = memory.read8(registers.sp().get())
                    val high = memory.read8(registers.sp().get() + 1)
                    registers.sp().set(registers.sp().get() + 2)
                    registers.pc().set(high.shl(8) + low)

                    timer.tick(meta.cycles().action())
                } else {
                    timer.tick(meta.cycles().none())
                }
            }
            0xD9 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                registers.pc().set(high.shl(8) + low)
                interrupts.enable()

                timer.tick(meta.cycles().action())
            }
        }
    }
}