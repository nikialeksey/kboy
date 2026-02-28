package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNext16
import com.alexeycode.kboy.gb.mem.readNext8

class Loads8Instruction(
    private val r: Registers,
    private val mem: Memory
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // 8-bit loads
            0x02 -> load({ r.a().get() }, { mem.write8(r.bc().get(), it) }, 8)
            0x12 -> load({ r.a().get() }, { mem.write8(r.de().get(), it) }, 8)
            0x0A -> load({ mem.read8(r.bc().get()) }, { r.a().set(it) }, 8)
            0x1A -> load({ mem.read8(r.de().get()) }, { r.a().set(it) }, 8)

            0x06 -> load({ mem.readNext8(r) }, { r.b().set(it) }, 8)
            0x0E -> load({ mem.readNext8(r) }, { r.c().set(it) }, 8)
            0x16 -> load({ mem.readNext8(r) }, { r.d().set(it) }, 8)
            0x1E -> load({ mem.readNext8(r) }, { r.e().set(it) }, 8)
            0x26 -> load({ mem.readNext8(r) }, { r.h().set(it) }, 8)
            0x2E -> load({ mem.readNext8(r) }, { r.l().set(it) }, 8)
            0x36 -> load({ mem.readNext8(r) }, { mem.write8(r.hl().get(), it) }, 12)
            0x3E -> load({ mem.readNext8(r) }, { r.a().set(it) }, 8)

            0x40 -> load({ r.b().get() }, { r.b().set(it) }, 4)
            0x41 -> load({ r.c().get() }, { r.b().set(it) }, 4)
            0x42 -> load({ r.d().get() }, { r.b().set(it) }, 4)
            0x43 -> load({ r.e().get() }, { r.b().set(it) }, 4)
            0x44 -> load({ r.h().get() }, { r.b().set(it) }, 4)
            0x45 -> load({ r.l().get() }, { r.b().set(it) }, 4)
            0x46 -> load({ mem.read8(r.hl().get()) }, { r.b().set(it) }, 8)
            0x47 -> load({ r.a().get() }, { r.b().set(it) }, 4)
            0x48 -> load({ r.b().get() }, { r.c().set(it) }, 4)
            0x49 -> load({ r.c().get() }, { r.c().set(it) }, 4)
            0x4A -> load({ r.d().get() }, { r.c().set(it) }, 4)
            0x4B -> load({ r.e().get() }, { r.c().set(it) }, 4)
            0x4C -> load({ r.h().get() }, { r.c().set(it) }, 4)
            0x4D -> load({ r.l().get() }, { r.c().set(it) }, 4)
            0x4E -> load({ mem.read8(r.hl().get()) }, { r.c().set(it) }, 8)
            0x4F -> load({ r.a().get() }, { r.c().set(it) }, 4)
            0x50 -> load({ r.b().get() }, { r.d().set(it) }, 4)
            0x51 -> load({ r.c().get() }, { r.d().set(it) }, 4)
            0x52 -> load({ r.d().get() }, { r.d().set(it) }, 4)
            0x53 -> load({ r.e().get() }, { r.d().set(it) }, 4)
            0x54 -> load({ r.h().get() }, { r.d().set(it) }, 4)
            0x55 -> load({ r.l().get() }, { r.d().set(it) }, 4)
            0x56 -> load({ mem.read8(r.hl().get()) }, { r.d().set(it) }, 8)
            0x57 -> load({ r.a().get() }, { r.d().set(it) }, 4)
            0x58 -> load({ r.b().get() }, { r.e().set(it) }, 4)
            0x59 -> load({ r.c().get() }, { r.e().set(it) }, 4)
            0x5A -> load({ r.d().get() }, { r.e().set(it) }, 4)
            0x5B -> load({ r.e().get() }, { r.e().set(it) }, 4)
            0x5C -> load({ r.h().get() }, { r.e().set(it) }, 4)
            0x5D -> load({ r.l().get() }, { r.e().set(it) }, 4)
            0x5E -> load({ mem.read8(r.hl().get()) }, { r.e().set(it) }, 8)
            0x5F -> load({ r.a().get() }, { r.e().set(it) }, 4)
            0x60 -> load({ r.b().get() }, { r.h().set(it) }, 4)
            0x61 -> load({ r.c().get() }, { r.h().set(it) }, 4)
            0x62 -> load({ r.d().get() }, { r.h().set(it) }, 4)
            0x63 -> load({ r.e().get() }, { r.h().set(it) }, 4)
            0x64 -> load({ r.h().get() }, { r.h().set(it) }, 4)
            0x65 -> load({ r.l().get() }, { r.h().set(it) }, 4)
            0x66 -> load({ mem.read8(r.hl().get()) }, { r.h().set(it) }, 8)
            0x67 -> load({ r.a().get() }, { r.h().set(it) }, 4)
            0x68 -> load({ r.b().get() }, { r.l().set(it) }, 4)
            0x69 -> load({ r.c().get() }, { r.l().set(it) }, 4)
            0x6A -> load({ r.d().get() }, { r.l().set(it) }, 4)
            0x6B -> load({ r.e().get() }, { r.l().set(it) }, 4)
            0x6C -> load({ r.h().get() }, { r.l().set(it) }, 4)
            0x6D -> load({ r.l().get() }, { r.l().set(it) }, 4)
            0x6E -> load({ mem.read8(r.hl().get()) }, { r.l().set(it) }, 8)
            0x6F -> load({ r.a().get() }, { r.l().set(it) }, 4)
            0x70 -> load({ r.b().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x71 -> load({ r.c().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x72 -> load({ r.d().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x73 -> load({ r.e().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x74 -> load({ r.h().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x75 -> load({ r.l().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x77 -> load({ r.a().get() }, { mem.write8(r.hl().get(), it)}, 8)
            0x78 -> load({ r.b().get() }, { r.a().set(it) }, 4)
            0x79 -> load({ r.c().get() }, { r.a().set(it) }, 4)
            0x7A -> load({ r.d().get() }, { r.a().set(it) }, 4)
            0x7B -> load({ r.e().get() }, { r.a().set(it) }, 4)
            0x7C -> load({ r.h().get() }, { r.a().set(it) }, 4)
            0x7D -> load({ r.l().get() }, { r.a().set(it) }, 4)
            0x7E -> load({ mem.read8(r.hl().get()) }, { r.a().set(it) }, 8)
            0x7F -> load({ r.a().get() }, { r.a().set(it) }, 4)

            0xE0 -> load({ r.a().get() }, { mem.write8(0xFF00 + mem.readNext8(r), it) }, 12)
            0xF0 -> load({ mem.read8(0xFF00 + mem.readNext8(r)) }, { r.a().set(it) }, 12)

            0xEA -> load({ r.a().get() }, { mem.write8(mem.readNext16(r), it) }, 16)
            0xFA -> load({ mem.read8(mem.readNext16(r)) }, { r.a().set(it) }, 16)

            0xE2 -> load({ r.a().get() }, { mem.write8(0xFF00 + r.c().get(), it) }, 8)
            0xF2 -> load({ mem.read8(0xFF00 + r.c().get()) }, { r.a().set(it) }, 8)

            0x22 -> load({ r.a().get() }, { mem.write8(r.hl().get(), it); r.hl().set(r.hl().get() + 1) }, 8)
            0x32 -> load({ r.a().get() }, { mem.write8(r.hl().get(), it); r.hl().set(r.hl().get() - 1) }, 8)
            0x2A -> load(
                {
                    val result = mem.read8(r.hl().get())
                    r.hl().set(r.hl().get() + 1)
                    result
                },
                { r.a().set(it) },
                8
            )
            0x3A -> load(
                {
                    val result = mem.read8(r.hl().get())
                    r.hl().set(r.hl().get() - 1)
                    result
                },
                { r.a().set(it) },
                8
            )
            else -> {
                0
            }
        }
    }

    private inline fun load(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val result = read()
        write(result.and(0xFF))

        return cycles
    }
}