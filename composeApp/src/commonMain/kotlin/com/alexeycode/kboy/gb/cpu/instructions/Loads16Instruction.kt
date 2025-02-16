package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNext16
import com.alexeycode.kboy.gb.mem.readNextSigned8

class Loads16Instruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // 16-bit load instructions
            0x01 -> load({ mem.readNext16(r) }, { r.bc().set(it) }, 12)
            0x11 -> load({ mem.readNext16(r) }, { r.de().set(it) }, 12)
            0x21 -> load({ mem.readNext16(r) }, { r.hl().set(it) }, 12)
            0x31 -> load({ mem.readNext16(r) }, { r.sp().set(it) }, 12)

            0x08 -> load({ r.sp().get() }, { mem.write8(mem.readNext16(r), it.and(0xFF)) }, 20)
            0xF8 -> {
                val a = r.sp().get()
                val b = mem.readNextSigned8(r)
                val result = a + b
                r.hl().set(result)

                r.flag().z().disable()
                r.flag().n().disable()
                // TODO i'm not sure that I should use here 8-bit comparison
                r.flag().h().setEnabled((a.and(0x0F) + b.and(0x0F)) > 0x0F)
                r.flag().c().setEnabled((a.and(0xFF) + b.and(0xFF)) > 0xFF)

                12
            }
            0xF9 -> load({ r.hl().get() }, { r.sp().set(it) }, 8)

            0xC1 -> pop({ r.bc().set(it) })
            0xD1 -> pop({ r.de().set(it) })
            0xE1 -> pop({ r.hl().set(it) })
            0xF1 -> pop({ r.af().set(it) }) // TODO it has flags? https://gbdev.io/gb-opcodes/optables/

            0xC5 -> push({ r.bc().get() })
            0xD5 -> push({ r.de().get() })
            0xE5 -> push({ r.hl().get() })
            0xF5 -> push({ r.af().get() })
            else -> {
                0
            }
        }
    }

    private fun load(read: () -> Int, write: (Int) -> Unit, cycles: Int): Int {
        val result = read()
        write(result)

        return cycles
    }

    private fun pop(write: (Int) -> Unit): Int {
        val low = mem.read8(r.sp().get())
        val high = mem.read8(r.sp().get() + 1)
        r.sp().set(r.sp().get() + 2)
        write(high.shl(8) + low)

        return 12
    }

    private fun push(read: () -> Int): Int {
        val value = read()
        mem.write8(r.sp().get() - 1, value.shr(8).and(0xFF))
        mem.write8(r.sp().get() - 2, value.and(0xFF))
        r.sp().set(r.sp().get() - 2)

        return 16
    }
}