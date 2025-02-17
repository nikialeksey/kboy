package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class ExtInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {
    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // 8-bit shift, rotate and bit instructions
            0x00 -> rlc({ r.b().get() }, { r.b().set(it) }, 8)
            0x01 -> rlc({ r.c().get() }, { r.c().set(it) }, 8)
            0x02 -> rlc({ r.d().get() }, { r.d().set(it) }, 8)
            0x03 -> rlc({ r.e().get() }, { r.e().set(it) }, 8)
            0x04 -> rlc({ r.h().get() }, { r.h().set(it) }, 8)
            0x05 -> rlc({ r.l().get() }, { r.l().set(it) }, 8)
            0x06 -> rlc({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x07 -> rlc({ r.a().get() }, { r.a().set(it) }, 8)

            0x08 -> rrc({ r.b().get() }, { r.b().set(it) }, 8)
            0x09 -> rrc({ r.c().get() }, { r.c().set(it) }, 8)
            0x0A -> rrc({ r.d().get() }, { r.d().set(it) }, 8)
            0x0B -> rrc({ r.e().get() }, { r.e().set(it) }, 8)
            0x0C -> rrc({ r.h().get() }, { r.h().set(it) }, 8)
            0x0D -> rrc({ r.l().get() }, { r.l().set(it) }, 8)
            0x0E -> rrc({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x0F -> rrc({ r.a().get() }, { r.a().set(it) }, 8)

            0x10 -> rl({ r.b().get() }, { r.b().set(it) }, 8)
            0x11 -> rl({ r.c().get() }, { r.c().set(it) }, 8)
            0x12 -> rl({ r.d().get() }, { r.d().set(it) }, 8)
            0x13 -> rl({ r.e().get() }, { r.e().set(it) }, 8)
            0x14 -> rl({ r.h().get() }, { r.h().set(it) }, 8)
            0x15 -> rl({ r.l().get() }, { r.l().set(it) }, 8)
            0x16 -> rl({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x17 -> rl({ r.a().get() }, { r.a().set(it) }, 8)

            0x18 -> rr({ r.b().get() }, { r.b().set(it) }, 8)
            0x19 -> rr({ r.c().get() }, { r.c().set(it) }, 8)
            0x1A -> rr({ r.d().get() }, { r.d().set(it) }, 8)
            0x1B -> rr({ r.e().get() }, { r.e().set(it) }, 8)
            0x1C -> rr({ r.h().get() }, { r.h().set(it) }, 8)
            0x1D -> rr({ r.l().get() }, { r.l().set(it) }, 8)
            0x1E -> rr({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x1F -> rr({ r.a().get() }, { r.a().set(it) }, 8)

            0x20 -> sla({ r.b().get() }, { r.b().set(it) }, 8)
            0x21 -> sla({ r.c().get() }, { r.c().set(it) }, 8)
            0x22 -> sla({ r.d().get() }, { r.d().set(it) }, 8)
            0x23 -> sla({ r.e().get() }, { r.e().set(it) }, 8)
            0x24 -> sla({ r.h().get() }, { r.h().set(it) }, 8)
            0x25 -> sla({ r.l().get() }, { r.l().set(it) }, 8)
            0x26 -> sla({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x27 -> sla({ r.a().get() }, { r.a().set(it) }, 8)

            0x28 -> sra({ r.b().get() }, { r.b().set(it) }, 8)
            0x29 -> sra({ r.c().get() }, { r.c().set(it) }, 8)
            0x2A -> sra({ r.d().get() }, { r.d().set(it) }, 8)
            0x2B -> sra({ r.e().get() }, { r.e().set(it) }, 8)
            0x2C -> sra({ r.h().get() }, { r.h().set(it) }, 8)
            0x2D -> sra({ r.l().get() }, { r.l().set(it) }, 8)
            0x2E -> sra({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x2F -> sra({ r.a().get() }, { r.a().set(it) }, 8)

            0x30 -> swap({ r.b().get() }, { r.b().set(it) }, 8)
            0x31 -> swap({ r.c().get() }, { r.c().set(it) }, 8)
            0x32 -> swap({ r.d().get() }, { r.d().set(it) }, 8)
            0x33 -> swap({ r.e().get() }, { r.e().set(it) }, 8)
            0x34 -> swap({ r.h().get() }, { r.h().set(it) }, 8)
            0x35 -> swap({ r.l().get() }, { r.l().set(it) }, 8)
            0x36 -> swap({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x37 -> swap({ r.a().get() }, { r.a().set(it) }, 8)

            0x38 -> srl({ r.b().get() }, { r.b().set(it) }, 8)
            0x39 -> srl({ r.c().get() }, { r.c().set(it) }, 8)
            0x3A -> srl({ r.d().get() }, { r.d().set(it) }, 8)
            0x3B -> srl({ r.e().get() }, { r.e().set(it) }, 8)
            0x3C -> srl({ r.h().get() }, { r.h().set(it) }, 8)
            0x3D -> srl({ r.l().get() }, { r.l().set(it) }, 8)
            0x3E -> srl({ mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x3F -> srl({ r.a().get() }, { r.a().set(it) }, 8)

            0x40 -> bit(0, { r.b().get() }, 8)
            0x41 -> bit(0, { r.c().get() }, 8)
            0x42 -> bit(0, { r.d().get() }, 8)
            0x43 -> bit(0, { r.e().get() }, 8)
            0x44 -> bit(0, { r.h().get() }, 8)
            0x45 -> bit(0, { r.l().get() }, 8)
            0x46 -> bit(0, { mem.read8(r.hl().get()) }, 12)
            0x47 -> bit(0, { r.a().get() }, 8)
            0x48 -> bit(1, { r.b().get() }, 8)
            0x49 -> bit(1, { r.c().get() }, 8)
            0x4A -> bit(1, { r.d().get() }, 8)
            0x4B -> bit(1, { r.e().get() }, 8)
            0x4C -> bit(1, { r.h().get() }, 8)
            0x4D -> bit(1, { r.l().get() }, 8)
            0x4E -> bit(1, { mem.read8(r.hl().get()) }, 12)
            0x4F -> bit(1, { r.a().get() }, 8)
            0x50 -> bit(2, { r.b().get() }, 8)
            0x51 -> bit(2, { r.c().get() }, 8)
            0x52 -> bit(2, { r.d().get() }, 8)
            0x53 -> bit(2, { r.e().get() }, 8)
            0x54 -> bit(2, { r.h().get() }, 8)
            0x55 -> bit(2, { r.l().get() }, 8)
            0x56 -> bit(2, { mem.read8(r.hl().get()) }, 12)
            0x57 -> bit(2, { r.a().get() }, 8)
            0x58 -> bit(3, { r.b().get() }, 8)
            0x59 -> bit(3, { r.c().get() }, 8)
            0x5A -> bit(3, { r.d().get() }, 8)
            0x5B -> bit(3, { r.e().get() }, 8)
            0x5C -> bit(3, { r.h().get() }, 8)
            0x5D -> bit(3, { r.l().get() }, 8)
            0x5E -> bit(3, { mem.read8(r.hl().get()) }, 12)
            0x5F -> bit(3, { r.a().get() }, 8)
            0x60 -> bit(4, { r.b().get() }, 8)
            0x61 -> bit(4, { r.c().get() }, 8)
            0x62 -> bit(4, { r.d().get() }, 8)
            0x63 -> bit(4, { r.e().get() }, 8)
            0x64 -> bit(4, { r.h().get() }, 8)
            0x65 -> bit(4, { r.l().get() }, 8)
            0x66 -> bit(4, { mem.read8(r.hl().get()) }, 12)
            0x67 -> bit(4, { r.a().get() }, 8)
            0x68 -> bit(5, { r.b().get() }, 8)
            0x69 -> bit(5, { r.c().get() }, 8)
            0x6A -> bit(5, { r.d().get() }, 8)
            0x6B -> bit(5, { r.e().get() }, 8)
            0x6C -> bit(5, { r.h().get() }, 8)
            0x6D -> bit(5, { r.l().get() }, 8)
            0x6E -> bit(5, { mem.read8(r.hl().get()) }, 12)
            0x6F -> bit(5, { r.a().get() }, 8)
            0x70 -> bit(6, { r.b().get() }, 8)
            0x71 -> bit(6, { r.c().get() }, 8)
            0x72 -> bit(6, { r.d().get() }, 8)
            0x73 -> bit(6, { r.e().get() }, 8)
            0x74 -> bit(6, { r.h().get() }, 8)
            0x75 -> bit(6, { r.l().get() }, 8)
            0x76 -> bit(6, { mem.read8(r.hl().get()) }, 12)
            0x77 -> bit(6, { r.a().get() }, 8)
            0x78 -> bit(7, { r.b().get() }, 8)
            0x79 -> bit(7, { r.c().get() }, 8)
            0x7A -> bit(7, { r.d().get() }, 8)
            0x7B -> bit(7, { r.e().get() }, 8)
            0x7C -> bit(7, { r.h().get() }, 8)
            0x7D -> bit(7, { r.l().get() }, 8)
            0x7E -> bit(7, { mem.read8(r.hl().get()) }, 12)
            0x7F -> bit(7, { r.a().get() }, 8)

            0x80 -> res(0, { r.b().get() }, { r.b().set(it) }, 8)
            0x81 -> res(0, { r.c().get() }, { r.c().set(it) }, 8)
            0x82 -> res(0, { r.d().get() }, { r.d().set(it) }, 8)
            0x83 -> res(0, { r.e().get() }, { r.e().set(it) }, 8)
            0x84 -> res(0, { r.h().get() }, { r.h().set(it) }, 8)
            0x85 -> res(0, { r.l().get() }, { r.l().set(it) }, 8)
            0x86 -> res(0, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x87 -> res(0, { r.a().get() }, { r.a().set(it) }, 8)
            0x88 -> res(1, { r.b().get() }, { r.b().set(it) }, 8)
            0x89 -> res(1, { r.c().get() }, { r.c().set(it) }, 8)
            0x8A -> res(1, { r.d().get() }, { r.d().set(it) }, 8)
            0x8B -> res(1, { r.e().get() }, { r.e().set(it) }, 8)
            0x8C -> res(1, { r.h().get() }, { r.h().set(it) }, 8)
            0x8D -> res(1, { r.l().get() }, { r.l().set(it) }, 8)
            0x8E -> res(1, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x8F -> res(1, { r.a().get() }, { r.a().set(it) }, 8)
            0x90 -> res(2, { r.b().get() }, { r.b().set(it) }, 8)
            0x91 -> res(2, { r.c().get() }, { r.c().set(it) }, 8)
            0x92 -> res(2, { r.d().get() }, { r.d().set(it) }, 8)
            0x93 -> res(2, { r.e().get() }, { r.e().set(it) }, 8)
            0x94 -> res(2, { r.h().get() }, { r.h().set(it) }, 8)
            0x95 -> res(2, { r.l().get() }, { r.l().set(it) }, 8)
            0x96 -> res(2, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x97 -> res(2, { r.a().get() }, { r.a().set(it) }, 8)
            0x98 -> res(3, { r.b().get() }, { r.b().set(it) }, 8)
            0x99 -> res(3, { r.c().get() }, { r.c().set(it) }, 8)
            0x9A -> res(3, { r.d().get() }, { r.d().set(it) }, 8)
            0x9B -> res(3, { r.e().get() }, { r.e().set(it) }, 8)
            0x9C -> res(3, { r.h().get() }, { r.h().set(it) }, 8)
            0x9D -> res(3, { r.l().get() }, { r.l().set(it) }, 8)
            0x9E -> res(3, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0x9F -> res(3, { r.a().get() }, { r.a().set(it) }, 8)
            0xA0 -> res(4, { r.b().get() }, { r.b().set(it) }, 8)
            0xA1 -> res(4, { r.c().get() }, { r.c().set(it) }, 8)
            0xA2 -> res(4, { r.d().get() }, { r.d().set(it) }, 8)
            0xA3 -> res(4, { r.e().get() }, { r.e().set(it) }, 8)
            0xA4 -> res(4, { r.h().get() }, { r.h().set(it) }, 8)
            0xA5 -> res(4, { r.l().get() }, { r.l().set(it) }, 8)
            0xA6 -> res(4, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xA7 -> res(4, { r.a().get() }, { r.a().set(it) }, 8)
            0xA8 -> res(5, { r.b().get() }, { r.b().set(it) }, 8)
            0xA9 -> res(5, { r.c().get() }, { r.c().set(it) }, 8)
            0xAA -> res(5, { r.d().get() }, { r.d().set(it) }, 8)
            0xAB -> res(5, { r.e().get() }, { r.e().set(it) }, 8)
            0xAC -> res(5, { r.h().get() }, { r.h().set(it) }, 8)
            0xAD -> res(5, { r.l().get() }, { r.l().set(it) }, 8)
            0xAE -> res(5, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xAF -> res(5, { r.a().get() }, { r.a().set(it) }, 8)
            0xB0 -> res(6, { r.b().get() }, { r.b().set(it) }, 8)
            0xB1 -> res(6, { r.c().get() }, { r.c().set(it) }, 8)
            0xB2 -> res(6, { r.d().get() }, { r.d().set(it) }, 8)
            0xB3 -> res(6, { r.e().get() }, { r.e().set(it) }, 8)
            0xB4 -> res(6, { r.h().get() }, { r.h().set(it) }, 8)
            0xB5 -> res(6, { r.l().get() }, { r.l().set(it) }, 8)
            0xB6 -> res(6, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xB7 -> res(6, { r.a().get() }, { r.a().set(it) }, 8)
            0xB8 -> res(7, { r.b().get() }, { r.b().set(it) }, 8)
            0xB9 -> res(7, { r.c().get() }, { r.c().set(it) }, 8)
            0xBA -> res(7, { r.d().get() }, { r.d().set(it) }, 8)
            0xBB -> res(7, { r.e().get() }, { r.e().set(it) }, 8)
            0xBC -> res(7, { r.h().get() }, { r.h().set(it) }, 8)
            0xBD -> res(7, { r.l().get() }, { r.l().set(it) }, 8)
            0xBE -> res(7, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xBF -> res(7, { r.a().get() }, { r.a().set(it) }, 8)

            0xC0 -> set(0, { r.b().get() }, { r.b().set(it) }, 8)
            0xC1 -> set(0, { r.c().get() }, { r.c().set(it) }, 8)
            0xC2 -> set(0, { r.d().get() }, { r.d().set(it) }, 8)
            0xC3 -> set(0, { r.e().get() }, { r.e().set(it) }, 8)
            0xC4 -> set(0, { r.h().get() }, { r.h().set(it) }, 8)
            0xC5 -> set(0, { r.l().get() }, { r.l().set(it) }, 8)
            0xC6 -> set(0, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xC7 -> set(0, { r.a().get() }, { r.a().set(it) }, 8)
            0xC8 -> set(1, { r.b().get() }, { r.b().set(it) }, 8)
            0xC9 -> set(1, { r.c().get() }, { r.c().set(it) }, 8)
            0xCA -> set(1, { r.d().get() }, { r.d().set(it) }, 8)
            0xCB -> set(1, { r.e().get() }, { r.e().set(it) }, 8)
            0xCC -> set(1, { r.h().get() }, { r.h().set(it) }, 8)
            0xCD -> set(1, { r.l().get() }, { r.l().set(it) }, 8)
            0xCE -> set(1, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xCF -> set(1, { r.a().get() }, { r.a().set(it) }, 8)
            0xD0 -> set(2, { r.b().get() }, { r.b().set(it) }, 8)
            0xD1 -> set(2, { r.c().get() }, { r.c().set(it) }, 8)
            0xD2 -> set(2, { r.d().get() }, { r.d().set(it) }, 8)
            0xD3 -> set(2, { r.e().get() }, { r.e().set(it) }, 8)
            0xD4 -> set(2, { r.h().get() }, { r.h().set(it) }, 8)
            0xD5 -> set(2, { r.l().get() }, { r.l().set(it) }, 8)
            0xD6 -> set(2, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xD7 -> set(2, { r.a().get() }, { r.a().set(it) }, 8)
            0xD8 -> set(3, { r.b().get() }, { r.b().set(it) }, 8)
            0xD9 -> set(3, { r.c().get() }, { r.c().set(it) }, 8)
            0xDA -> set(3, { r.d().get() }, { r.d().set(it) }, 8)
            0xDB -> set(3, { r.e().get() }, { r.e().set(it) }, 8)
            0xDC -> set(3, { r.h().get() }, { r.h().set(it) }, 8)
            0xDD -> set(3, { r.l().get() }, { r.l().set(it) }, 8)
            0xDE -> set(3, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xDF -> set(3, { r.a().get() }, { r.a().set(it) }, 8)
            0xE0 -> set(4, { r.b().get() }, { r.b().set(it) }, 8)
            0xE1 -> set(4, { r.c().get() }, { r.c().set(it) }, 8)
            0xE2 -> set(4, { r.d().get() }, { r.d().set(it) }, 8)
            0xE3 -> set(4, { r.e().get() }, { r.e().set(it) }, 8)
            0xE4 -> set(4, { r.h().get() }, { r.h().set(it) }, 8)
            0xE5 -> set(4, { r.l().get() }, { r.l().set(it) }, 8)
            0xE6 -> set(4, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xE7 -> set(4, { r.a().get() }, { r.a().set(it) }, 8)
            0xE8 -> set(5, { r.b().get() }, { r.b().set(it) }, 8)
            0xE9 -> set(5, { r.c().get() }, { r.c().set(it) }, 8)
            0xEA -> set(5, { r.d().get() }, { r.d().set(it) }, 8)
            0xEB -> set(5, { r.e().get() }, { r.e().set(it) }, 8)
            0xEC -> set(5, { r.h().get() }, { r.h().set(it) }, 8)
            0xED -> set(5, { r.l().get() }, { r.l().set(it) }, 8)
            0xEE -> set(5, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xEF -> set(5, { r.a().get() }, { r.a().set(it) }, 8)
            0xF0 -> set(6, { r.b().get() }, { r.b().set(it) }, 8)
            0xF1 -> set(6, { r.c().get() }, { r.c().set(it) }, 8)
            0xF2 -> set(6, { r.d().get() }, { r.d().set(it) }, 8)
            0xF3 -> set(6, { r.e().get() }, { r.e().set(it) }, 8)
            0xF4 -> set(6, { r.h().get() }, { r.h().set(it) }, 8)
            0xF5 -> set(6, { r.l().get() }, { r.l().set(it) }, 8)
            0xF6 -> set(6, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xF7 -> set(6, { r.a().get() }, { r.a().set(it) }, 8)
            0xF8 -> set(7, { r.b().get() }, { r.b().set(it) }, 8)
            0xF9 -> set(7, { r.c().get() }, { r.c().set(it) }, 8)
            0xFA -> set(7, { r.d().get() }, { r.d().set(it) }, 8)
            0xFB -> set(7, { r.e().get() }, { r.e().set(it) }, 8)
            0xFC -> set(7, { r.h().get() }, { r.h().set(it) }, 8)
            0xFD -> set(7, { r.l().get() }, { r.l().set(it) }, 8)
            0xFE -> set(7, { mem.read8(r.hl().get()) }, { mem.write8(r.hl().get(), it) }, 16)
            0xFF -> set(7, { r.a().get() }, { r.a().set(it) }, 8)

            else -> {
                0
            }
        }
    }

    private inline fun rlc(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = if (n.and(0b1000_0000) == 0) 0 else 1
        val result = (n.shl(1) + c).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun rrc(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = n.and(0b0000_0001)
        val result = (n.and(0xFF).shr(1) + c.shl(7)).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun rl(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = if (n.and(0b1000_0000) == 0) 0 else 1
        val prevC = if (r.flag().c().isEnabled()) 1 else 0
        val result = (n.shl(1) + prevC).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun rr(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = n.and(0b0000_0001)
        val prevC = if (r.flag().c().isEnabled()) 1 else 0
        val result = (n.and(0xFF).shr(1) + prevC.shl(7)).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun sla(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = if (n.and(0b1000_0000) == 0) 0 else 1
        val result = n.shl(1).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun sra(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = n.and(0b0000_0001)
        val result = n.and(0xFF).shr(1).and(0xFF) + n.and(0b1000_0000)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun swap(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val result = n.and(0x0F).shl(4) + n.and(0xF0).shr(4)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().disable()

        return cycles
    }

    private inline fun srl(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val n = read()
        val c = n.and(0b0000_0001)
        val result = n.and(0xFF).shr(1).and(0xFF)
        write(result)

        r.flag().z().setEnabled(result == 0)
        r.flag().n().disable()
        r.flag().h().disable()
        r.flag().c().setEnabled(c == 1)

        return cycles
    }

    private inline fun bit(n: Int, read: () -> Int, cycles: Int): Int {
        val a = read()
        val result = a.and(1.shl(n)) != 0

        r.flag().z().setEnabled(!result)
        r.flag().n().disable()
        r.flag().h().enable()

        return cycles
    }

    private inline fun res(n: Int, read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val a = read()
        write(a.and(1.shl(n).inv()).and(0xFF))

        return cycles
    }

    private inline fun set(n: Int, read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val a = read()
        write(a.or(1.shl(n)).and(0xFF))

        return cycles
    }
}