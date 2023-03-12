package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.instructions.operands.Operand
import pro.devdesign.gameboy.cpu.opcodes.InstructionMeta
import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class Alu8Instruction : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>,
        memory: Memory,
        registers: Registers
    ) {
        when (meta.opcode()) {
            // 8-bit arithmetic / logical instructions
            // 8-bit increments
            0x04, 0x0C, 0x14, 0x1C, 0x24, 0x2C, 0x34, 0x3C -> {
                val value = operands[0].read8(memory, registers)
                val result = (value + 1).and(0xFF)
                operands[0].write8(memory, registers, result)
                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((value.and(0x0F) + 1.and(0x0F)) > 0x0F)
            }
            // 8-bit decrements
            0x05, 0x0D, 0x15, 0x1D, 0x25, 0x2D, 0x35, 0x3D -> {
                val value = operands[0].read8(memory, registers)
                val result = (value - 1).and(0xFF)
                operands[0].write8(memory, registers, result)
                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((value.and(0x0F) - 1.and(0x0F)) < 0)
            }
            // 8-bit add
            in 0x80..0x87, 0xC6 -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a + n).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)
            }
            // 8-bit adc
            in 0x88..0x8F, 0xCE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val c = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (a + n + c).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F) + c) > 0x0F)
                registers.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF) + c) > 0xFF)
            }
            // 8-bit sub
            in 0x90..0x97, 0xD6 -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a - n).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)
            }
            // 8-bit sbc
            in 0x98..0x9F, 0xDE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val c = if (registers.flag().c().isEnabled()) 1 else 0
                val result = (a - n - c).and(0xFF)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F) - c) < 0)
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF) - c) < 0)
            }
            // 8-bit and
            in 0xA0..0xA7, 0xE6 -> {
                val a = registers.a().get()
                val n = operands[0].read8(memory, registers)
                val result = a.and(n)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().enable()
                registers.flag().c().disable()
            }
            // 8-bit xor
            in 0xA8..0xAF, 0xEE -> {
                val a = registers.a().get()
                val n = operands[0].read8(memory, registers)
                val result = a.xor(n)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().disable()
            }
            // 8-bit or
            in 0xB0..0xB7, 0xF6 -> {
                val a = registers.a().get()
                val n = operands[0].read8(memory, registers)
                val result = a.or(n)
                operands[0].write8(memory, registers, result)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().disable()
                registers.flag().h().disable()
                registers.flag().c().disable()
            }
            // 8-bit cp
            in 0xB8..0xBF, 0xFE -> {
                val a = operands[0].read8(memory, registers)
                val n = operands[1].read8(memory, registers)
                val result = (a - n).and(0xFF)

                registers.flag().z().setEnabled(result == 0)
                registers.flag().n().enable()
                registers.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                registers.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)
            }
        }
    }
}