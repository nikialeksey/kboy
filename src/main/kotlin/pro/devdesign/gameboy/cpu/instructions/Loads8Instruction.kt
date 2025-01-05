package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.cpu.timer.Timer
import pro.devdesign.gameboy.mem.Memory
import pro.devdesign.gameboy.mem.OffsetMemory

class Loads8Instruction : Instruction {

    private val registers: Registers
    private val memory: Memory
    private val memoryFF: Memory
    private val timer: Timer

    constructor(
        registers: Registers,
        memory: Memory,
        timer: Timer
    ) : this(
        registers,
        memory,
        OffsetMemory(memory, 0xFF00),
        timer
    )

    constructor(
        registers: Registers,
        memory: Memory,
        memoryFF: Memory,
        timer: Timer
    ) {
        this.registers = registers
        this.memory = memory
        this.memoryFF = memoryFF
        this.timer = timer
    }

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ) {
        when (meta.opcode()) {
            // 8-bit loads
            0x02, 0x12,
            0x0A, 0x1A,
            0x06, 0x0E, 0x16, 0x1E,
            0x26, 0x2E, 0x36, 0x3E,
            in 0x40..0x75, in 0x77..0x7F,
            0xEA, 0xFA -> {
                val result = operands[1].read8(memory, registers)
                operands[0].write8(memory, registers, result.and(0xFF))

                timer.tick(meta.cycles().action())
            }
            0xE0, 0xF0, 0xE2, 0xF2 -> {
                val result = operands[1].read8(memoryFF, registers)
                operands[0].write8(memory, registers, result.and(0xFF))

                timer.tick(meta.cycles().action())
            }
            0x22, 0x32, 0x2A, 0x3A -> {
                val result = operands[1].read8(memory, registers)
                operands[0].write8(memory, registers, result.and(0xFF))
                if (meta.opcode() == 0x22 || meta.opcode() == 0x32) {
                    operands[0].incOrDec(registers)
                } else {
                    operands[1].incOrDec(registers)
                }

                timer.tick(meta.cycles().action())
            }
        }
    }
}