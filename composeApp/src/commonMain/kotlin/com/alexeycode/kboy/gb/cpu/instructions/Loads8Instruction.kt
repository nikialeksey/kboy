package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.OffsetMemory

class Loads8Instruction : Instruction {

    private val r: Registers
    private val mem: Memory
    private val memFF: Memory

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
        this.r = registers
        this.mem = memory
        this.memFF = memoryFF
    }

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        val opcode = meta.opcode()
        return when (opcode) {
            // 8-bit loads
            0x02, 0x12,
            0x0A, 0x1A -> {
                val result = operands[1].read8(mem, r)
                operands[0].write8(mem, r, result.and(0xFF))

                8
            }
            0x06, 0x0E, 0x16, 0x1E,
            0x26, 0x2E, 0x36, 0x3E -> {
                val result = operands[1].read8(mem, r)
                operands[0].write8(mem, r, result.and(0xFF))

                if (opcode == 0x36) {
                    12
                } else {
                    8
                }
            }
            in 0x40..0x75, in 0x77..0x7F -> {
                val result = operands[1].read8(mem, r)
                operands[0].write8(mem, r, result.and(0xFF))

                if (opcode == 0x46 || opcode == 0x56 || opcode == 0x66 || opcode in 0x70..0x75 || opcode == 0x77 || opcode == 0x4E || opcode == 0x5E || opcode == 0x6E || opcode == 0x7E) {
                    8
                } else {
                    4
                }
            }
            0xEA, 0xFA -> {
                val result = operands[1].read8(mem, r)
                operands[0].write8(mem, r, result.and(0xFF))

                16
            }
            0xE0, 0xF0 -> {
                val result = operands[1].read8(memFF, r)
                operands[0].write8(mem, r, result.and(0xFF))

                12
            }
            0xE2, 0xF2 -> {
                val result = operands[1].read8(memFF, r)
                operands[0].write8(mem, r, result.and(0xFF))

                8
            }
            0x22, 0x32, 0x2A, 0x3A -> {
                val result = operands[1].read8(mem, r)
                operands[0].write8(mem, r, result.and(0xFF))
                if (opcode == 0x22 || opcode == 0x32) {
                    operands[0].incOrDec(r)
                } else {
                    operands[1].incOrDec(r)
                }

                8
            }
            else -> {
                0
            }
        }
    }
}