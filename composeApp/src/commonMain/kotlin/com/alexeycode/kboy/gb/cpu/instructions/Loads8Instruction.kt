package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.OffsetMemory

class Loads8Instruction : Instruction {

    private val registers: Registers
    private val memory: Memory
    private val memoryFF: Memory

    constructor(
        registers: Registers,
        memory: Memory,
    ) : this(
        registers,
        memory,
        OffsetMemory(memory, 0xFF00),
    )

    constructor(
        registers: Registers,
        memory: Memory,
        memoryFF: Memory,
    ) {
        this.registers = registers
        this.memory = memory
        this.memoryFF = memoryFF
    }

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        return when (meta.opcode()) {
            // 8-bit loads
            0x02, 0x12,
            0x0A, 0x1A,
            0x06, 0x0E, 0x16, 0x1E,
            0x26, 0x2E, 0x36, 0x3E,
            in 0x40..0x75, in 0x77..0x7F,
            0xEA, 0xFA -> {
                val result = operands[1].read8(memory, registers)
                operands[0].write8(memory, registers, result.and(0xFF))

                meta.cycles().action()
            }
            0xE0, 0xF0, 0xE2, 0xF2 -> {
                val result = operands[1].read8(memoryFF, registers)
                operands[0].write8(memory, registers, result.and(0xFF))

                meta.cycles().action()
            }
            0x22, 0x32, 0x2A, 0x3A -> {
                val result = operands[1].read8(memory, registers)
                operands[0].write8(memory, registers, result.and(0xFF))
                if (meta.opcode() == 0x22 || meta.opcode() == 0x32) {
                    operands[0].incOrDec(registers)
                } else {
                    operands[1].incOrDec(registers)
                }

                meta.cycles().action()
            }
            else -> {
                0
            }
        }
    }
}