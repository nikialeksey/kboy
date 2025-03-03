package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNext8

class Alu8Instruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // 8-bit arithmetic / logical instructions
            // 8-bit increments
            0x04 -> increment({ r.b().get() }, { r.b().set(it) }, 4)
            0x0C -> increment({ r.c().get() }, { r.c().set(it) }, 4)
            0x14 -> increment({ r.d().get() }, { r.d().set(it) }, 4)
            0x1C -> increment({ r.e().get() }, { r.e().set(it) }, 4)
            0x24 -> increment({ r.h().get() }, { r.h().set(it) }, 4)
            0x2C -> increment({ r.l().get() }, { r.l().set(it) }, 4)
            0x34 -> increment({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 12)
            0x3C -> increment({ r.a().get() }, { r.a().set(it) }, 4)

            // 8-bit decrements
            0x05 -> decrement({ r.b().get() }, { r.b().set(it) }, 4)
            0x0D -> decrement({ r.c().get() }, { r.c().set(it) }, 4)
            0x15 -> decrement({ r.d().get() }, { r.d().set(it) }, 4)
            0x1D -> decrement({ r.e().get() }, { r.e().set(it) }, 4)
            0x25 -> decrement({ r.h().get() }, { r.h().set(it) }, 4)
            0x2D -> decrement({ r.l().get() }, { r.l().set(it) }, 4)
            0x35 -> decrement({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 12)
            0x3D -> decrement({ r.a().get() }, { r.a().set(it) }, 4)

            // 8-bit add
            0x80 -> add({ r.a().get() }, { r.b().get() }, 4)
            0x81 -> add({ r.a().get() }, { r.c().get() }, 4)
            0x82 -> add({ r.a().get() }, { r.d().get() }, 4)
            0x83 -> add({ r.a().get() }, { r.e().get() }, 4)
            0x84 -> add({ r.a().get() }, { r.h().get() }, 4)
            0x85 -> add({ r.a().get() }, { r.l().get() }, 4)
            0x86 -> add({ r.a().get() }, { mem.read8(r.hl().get()) }, 8)
            0x87 -> add({ r.a().get() }, { r.a().get() }, 4)
            0xC6 -> add({ r.a().get() }, { mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit adc
            0x88 -> adc({ r.a().get() }, { r.b().get() }, 4)
            0x89 -> adc({ r.a().get() }, { r.c().get() }, 4)
            0x8A -> adc({ r.a().get() }, { r.d().get() }, 4)
            0x8B -> adc({ r.a().get() }, { r.e().get() }, 4)
            0x8C -> adc({ r.a().get() }, { r.h().get() }, 4)
            0x8D -> adc({ r.a().get() }, { r.l().get() }, 4)
            0x8E -> adc({ r.a().get() }, { mem.read8(r.hl().get()) }, 8)
            0x8F -> adc({ r.a().get() }, { r.a().get() }, 4)
            0xCE -> adc({ r.a().get() }, { mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit sub
            0x90 -> sub({ r.a().get() }, { r.b().get() }, 4)
            0x91 -> sub({ r.a().get() }, { r.c().get() }, 4)
            0x92 -> sub({ r.a().get() }, { r.d().get() }, 4)
            0x93 -> sub({ r.a().get() }, { r.e().get() }, 4)
            0x94 -> sub({ r.a().get() }, { r.h().get() }, 4)
            0x95 -> sub({ r.a().get() }, { r.l().get() }, 4)
            0x96 -> sub({ r.a().get() }, { mem.read8(r.hl().get()) }, 8)
            0x97 -> sub({ r.a().get() }, { r.a().get() }, 4)
            0xD6 -> sub({ r.a().get() }, { mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit sbc
            0x98 -> sbc({ r.a().get() }, { r.b().get() }, 4)
            0x99 -> sbc({ r.a().get() }, { r.c().get() }, 4)
            0x9A -> sbc({ r.a().get() }, { r.d().get() }, 4)
            0x9B -> sbc({ r.a().get() }, { r.e().get() }, 4)
            0x9C -> sbc({ r.a().get() }, { r.h().get() }, 4)
            0x9D -> sbc({ r.a().get() }, { r.l().get() }, 4)
            0x9E -> sbc({ r.a().get() }, { mem.read8(r.hl().get()) }, 8)
            0x9F -> sbc({ r.a().get() }, { r.a().get() }, 4)
            0xDE -> sbc({ r.a().get() }, { mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit and
            0xA0 -> and({ r.b().get() }, 4)
            0xA1 -> and({ r.c().get() }, 4)
            0xA2 -> and({ r.d().get() }, 4)
            0xA3 -> and({ r.e().get() }, 4)
            0xA4 -> and({ r.h().get() }, 4)
            0xA5 -> and({ r.l().get() }, 4)
            0xA6 -> and({ mem.read8(r.hl().get()) }, 8)
            0xA7 -> and({ r.a().get() }, 4)
            0xE6 -> and({ mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit xor
            0xA8 -> xor({ r.b().get() }, 4)
            0xA9 -> xor({ r.c().get() }, 4)
            0xAA -> xor({ r.d().get() }, 4)
            0xAB -> xor({ r.e().get() }, 4)
            0xAC -> xor({ r.h().get() }, 4)
            0xAD -> xor({ r.l().get() }, 4)
            0xAE -> xor({ mem.read8(r.hl().get()) }, 8)
            0xAF -> xor({ r.a().get() }, 4)
            0xEE -> xor({ mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit or
            0xB0 -> or({ r.b().get() }, 4)
            0xB1 -> or({ r.c().get() }, 4)
            0xB2 -> or({ r.d().get() }, 4)
            0xB3 -> or({ r.e().get() }, 4)
            0xB4 -> or({ r.h().get() }, 4)
            0xB5 -> or({ r.l().get() }, 4)
            0xB6 -> or({ mem.read8(r.hl().get()) }, 8)
            0xB7 -> or({ r.a().get() }, 4)
            0xF6 -> or({ mem.readNext8(r) /* n8 */ }, 8)

            // 8-bit cp
            0xB8 -> cp({ r.b().get() }, 4)
            0xB9 -> cp({ r.c().get() }, 4)
            0xBA -> cp({ r.d().get() }, 4)
            0xBB -> cp({ r.e().get() }, 4)
            0xBC -> cp({ r.h().get() }, 4)
            0xBD -> cp({ r.l().get() }, 4)
            0xBE -> cp({ mem.read8(r.hl().get()) }, 8)
            0xBF -> cp({ r.a().get() }, 4)
            0xFE -> cp({ mem.readNext8(r) /* n8 */ }, 8)

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

    private inline fun add(read1: () -> Int, read2: () -> Int, cycles: Int): Int {
        val a = read1()
        val n = read2()
        val result = (a + n).and(0xFF)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
        r.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)

        return cycles
    }

    private inline fun adc(read1: () -> Int, read2: () -> Int, cycles: Int): Int {
        val a = read1()
        val n = read2()
        val c = if (r.flag().c().isEnabled()) 1 else 0
        val result = (a + n + c).and(0xFF)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F) + c) > 0x0F)
        r.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF) + c) > 0xFF)

        return cycles
    }

    private inline fun sub(read1: () -> Int, read2: () -> Int, cycles: Int): Int {
        val a = read1()
        val n = read2()
        val result = (a - n).and(0xFF)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().enable()
        r.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F)) < 0)
        r.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF)) < 0)

        return cycles
    }

    private inline fun sbc(read1: () -> Int, read2: () -> Int, cycles: Int): Int {
        val a = read1()
        val n = read2()
        val c = if (r.flag().c().isEnabled()) 1 else 0
        val result = (a - n - c).and(0xFF)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().enable()
//        r.flag().h().setEnabled((a.and(0x0F) - n.and(0x0F) - c) < 0)
        r.flag().h().setEnabled(((a xor n xor (result and 0xff)) and (1 shl 4)) != 0)
        r.flag().c().setEnabled((a.and(0xFF) - n.and(0xFF) - c) < 0)

        return cycles
    }

    private inline fun and(read: () -> Int, cycles: Int): Int {
        val a = r.a().get()
        val n = read()
        val result = a.and(n)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().enable()
        r.flag().c().disable()

        return cycles
    }

    private inline fun xor(read: () -> Int, cycles: Int): Int {
        val a = r.a().get()
        val n = read()
        val result = a.xor(n)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().disable()

        return cycles
    }

    private inline fun or(read: () -> Int, cycles: Int): Int {
        val a = r.a().get()
        val n = read()
        val result = a.or(n)
        r.a().set(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().disable()

        return cycles
    }

    private inline fun cp(read: () -> Int, cycles: Int): Int {
        val a = r.a().get()
        val n = read()
        val result = (a - n).and(0xFF)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().enable()
        r.flag().h().setEnabled(n.and(0x0F) > a.and(0x0F))
        r.flag().c().setEnabled(n.and(0xFF) > a.and(0xFF))

        return cycles
    }
}