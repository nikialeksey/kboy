package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class CallsInstruction(
    private val registers: Registers,
    private val memory: Memory
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ) {
        when (meta.opcode()) {
            // Calls
            0xCD -> {
                val pc = registers.pc().get()
                memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                registers.sp().set(registers.sp().get() - 2)
                registers.pc().set(operands[0].read16(memory, registers))
            }
            0xC4, 0xCC, 0xD4, 0xDC -> {
                if (operands[0].check(registers)) {
                    val pc = registers.pc().get()
                    memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
                    memory.write8(registers.sp().get() - 2, pc.and(0xFF))
                    registers.sp().set(registers.sp().get() - 2)
                    registers.pc().set(operands[1].read16(memory, registers))
                }
            }
        }
    }
}