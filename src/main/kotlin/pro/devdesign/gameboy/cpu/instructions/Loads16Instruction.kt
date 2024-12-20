package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class Loads16Instruction(
    private val registers: Registers,
    private val memory: Memory
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ) {
        when (meta.opcode()) {
            // 16-bit load instructions
            0x01, 0x11, 0x21, 0x31, 0xF9, 0x08 -> {
                val result = operands[1].read16(memory, registers)
                operands[0].write16(memory, registers, result)
            }
            0xF8 -> {
                val result = operands[1].read16(memory, registers) + operands[2].read8(memory, registers)
                operands[0].write16(memory, registers, result)
            }
            0xC1, 0xD1, 0xE1, 0xF1 -> {
                val low = memory.read8(registers.sp().get())
                val high = memory.read8(registers.sp().get() + 1)
                registers.sp().set(registers.sp().get() + 2)
                operands[0].write16(memory, registers, high.shl(8) + low)
            }
            0xC5, 0xD5, 0xE5, 0xF5 -> {
                val value = operands[0].read16(memory, registers)
                memory.write8(registers.sp().get() - 1, value.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, value.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)
            }
        }
    }
}