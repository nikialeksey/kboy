package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class ReturnsInstruction(
    private val registers: Registers,
    private val memory: Memory
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
            }
            0xC0, 0xC8, 0xD0, 0xD8 -> {
                if (operands[0].check(registers)) {
                    val low = memory.read8(registers.sp().get())
                    val high = memory.read8(registers.sp().get() + 1)
                    registers.sp().set(registers.sp().get() + 2)
                    registers.pc().set(high.shl(8) + low)
                }
            }
            0xD9 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                registers.pc().set(high.shl(8) + low)
                // then enable interrupts
            }
        }
    }
}