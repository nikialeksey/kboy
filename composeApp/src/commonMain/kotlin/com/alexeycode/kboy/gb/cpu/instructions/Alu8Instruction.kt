package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class Alu8Instruction(
    private val r: Registers,
    private val memory: Memory,
) : Instruction {

    override fun execute(
        meta: InstructionMeta,
        operands: List<Operand>
    ): Int {
        val opcode = meta.opcode()
        return when (opcode) {
            // 8-bit arithmetic / logical instructions
            // 8-bit increments
            0x04 -> increment({ r.b().get() }, { r.b().set(it) }, 4)
            0x0C -> increment({ r.c().get() }, { r.c().set(it) }, 4)
            0x14 -> increment({ r.d().get() }, { r.d().set(it) }, 4)
            0x1C -> increment({ r.e().get() }, { r.e().set(it) }, 4)
            0x24 -> increment({ r.h().get() }, { r.h().set(it) }, 4)
            0x2C -> increment({ r.l().get() }, { r.l().set(it) }, 4)
            0x34 -> increment({ memory.read8(r.hl().get()) }, { memory.write8(r.hl().get(), it) }, 12)
            0x3C -> increment({ r.a().get() }, { r.a().set(it) }, 4)

            // 8-bit decrements
            0x05 -> decrement({ r.b().get() }, { r.b().set(it) }, 4)
            0x0D -> decrement({ r.c().get() }, { r.c().set(it) }, 4)
            0x15 -> decrement({ r.d().get() }, { r.d().set(it) }, 4)
            0x1D -> decrement({ r.e().get() }, { r.e().set(it) }, 4)
            0x25 -> decrement({ r.h().get() }, { r.h().set(it) }, 4)
            0x2D -> decrement({ r.l().get() }, { r.l().set(it) }, 4)
            0x35 -> decrement({ memory.read8(r.hl().get()) }, { memory.write8(r.hl().get(), it) }, 12)
            0x3D -> decrement({ r.a().get() }, { r.a().set(it) }, 4)

            // 8-bit add
            0x80 -> add({ r.a().get() }, { r.b().get() }, { r.a().set(it) }, 4)
            0x81 -> add({ r.a().get() }, { r.c().get() }, { r.a().set(it) }, 4)
            0x82 -> add({ r.a().get() }, { r.d().get() }, { r.a().set(it) }, 4)
            0x83 -> add({ r.a().get() }, { r.e().get() }, { r.a().set(it) }, 4)
            0x84 -> add({ r.a().get() }, { r.h().get() }, { r.a().set(it) }, 4)
            0x85 -> add({ r.a().get() }, { r.l().get() }, { r.a().set(it) }, 4)
            0x86 -> add({ r.a().get() }, { memory.read8(r.hl().get()) }, { r.a().set(it) }, 8)
            0x87 -> add({ r.a().get() }, { r.a().get() }, { r.a().set(it) }, 4)
            0xC6 -> add({ r.a().get() }, { operands[1].read8(memory, r) }, { r.a().set(it) }, 8)

            // 8-bit adc
            in 0x88..0x8F, 0xCE -> {
                val a = operands[0].read8(memory, r)
                val n = operands[1].read8(memory, r)
                val c = if (r.flag().c().isEnabled()) 1 else 0
                val result = (a + n + c).and(0xFF)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F) + c) > 0x0F)
                r.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF) + c) > 0xFF)

                meta.cycles().action()
            }
            // 8-bit sub
            in 0x90..0x97, 0xD6 -> {
                val a = operands[0].read8(memory, r)
                val n = operands[1].read8(memory, r)
                val result = (a - n).and(0xFF)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().enable()
                r.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                r.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)

                meta.cycles().action()
            }
            // 8-bit sbc
            in 0x98..0x9F, 0xDE -> {
                val a = operands[0].read8(memory, r)
                val n = operands[1].read8(memory, r)
                val c = if (r.flag().c().isEnabled()) 1 else 0
                val result = (a - n - c).and(0xFF)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().enable()
                r.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F) - c) < 0)
                // TODO interesting, but here C flag is not always updated:
                // https://github.com/gbdev/gb-opcodes/commit/d091dd1d4a4b9fe783b53d19a8b0c50a28c0c92c
                r.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF) - c) < 0)

                meta.cycles().action()
            }
            // 8-bit and
            in 0xA0..0xA7, 0xE6 -> {
                val a = r.a().get()
                val n = operands[1].read8(memory, r)
                val result = a.and(n)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().enable()
                r.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit xor
            in 0xA8..0xAF, 0xEE -> {
                val a = r.a().get()
                val n = operands[1].read8(memory, r)
                val result = a.xor(n)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit or
            in 0xB0..0xB7, 0xF6 -> {
                val a = r.a().get()
                val n = operands[1].read8(memory, r)
                val result = a.or(n)
                r.a().set(result)
                operands[0].write8(memory, r, result)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().disable()
                r.flag().h().disable()
                r.flag().c().disable()

                meta.cycles().action()
            }
            // 8-bit cp
            in 0xB8..0xBF, 0xFE -> {
                val a = operands[0].read8(memory, r)
                val n = operands[1].read8(memory, r)
                val result = (a - n).and(0xFF)

                r.flag().z().setEnabled(result == 0)
                r.flag().n().enable()
                r.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
                r.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)

                meta.cycles().action()
            }

            else -> {
                0
            }
        }
    }

    private inline fun increment(
        read: () -> Int,
        write: (Int) -> Unit,
        cycles: Int
    ): Int {
        val value = read()
        val result = (value + 1).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().setEnabled((value.and(0x0F) + 1.and(0x0F)) > 0x0F)

        return cycles
    }

    private inline fun decrement(
        read: () -> Int,
        write: (Int) -> Unit,
        cycles: Int
    ): Int {
        val value = read()
        val result = (value - 1).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().enable()
        r.flag().h().setEnabled((value.and(0x0F) - 1.and(0x0F)) < 0)

        return cycles
    }

    private inline fun add(read1: () -> Int, read2: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val a = read1()
        val n = read2()
        val result = (a + n).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
        r.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)

        return cycles
    }
}