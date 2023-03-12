package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class RestartsInstruction : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    ) {
        when (meta.opcode()) {
            // Restarts
            0xC7, 0xCF, 0xD7, 0xDF, 0xE7, 0xEF, 0xF7, 0xFF -> {
                val pc = registers.pc().get() + meta.bytes()
                memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)
                registers.pc().set(operands[0].read16(memory, registers))
            }
        }
    }
}