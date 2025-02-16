package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Register
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNextSigned8

class Alu16Instruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // 16-bit arithmetic / logical instructions
            // 16-bit inc
            0x03 -> inc(r.bc())
            0x13 -> inc(r.de())
            0x23 -> inc(r.hl())
            0x33 -> inc(r.sp())

            // 16-bit dec
            0x0B -> dec(r.bc())
            0x1B -> dec(r.de())
            0x2B -> dec(r.hl())
            0x3B -> dec(r.sp())

            // 16-bit add
            0x09 -> add(r.bc())
            0x19 -> add(r.de())
            0x29 -> add(r.hl())
            0x39 -> add(r.sp())

            0xE8 -> {
                val a = r.sp().get()
                val n = mem.readNextSigned8(r) /* e8 */
                val result = (a + n).and(0xFFFF)
                r.sp().set(result)

                r.flag().z().disable()
                r.flag().n().disable()
                r.flag().h().setEnabled((a.and(0x0F) + n.and(0x0F)) > 0x0F)
                r.flag().c().setEnabled((a.and(0xFF) + n.and(0xFF)) > 0xFF)

                16
            }
            else -> {
                0
            }
        }
    }

    private fun inc(r: Register): Int {
        val value = r.get()
        val result = (value + 1).and(0xFFFF)
        r.set(result)

        return 8
    }

    private fun dec(r: Register): Int {
        val value = r.get()
        val result = (value - 1).and(0xFFFF)
        r.set(result)

        return 8
    }

    private fun add(r: Register): Int {
        val a = this.r.hl().get()
        val n = r.get()
        val result = (a + n).and(0xFFFF)
        this.r.hl().set(result)

        this.r.flag().n().disable()
        this.r.flag().h().setEnabled((a.and(0x0FFF) + n.and(0x0FFF)) > 0x0FFF)
        this.r.flag().c().setEnabled((a + n) > 0xFFFF)

        return 8
    }
}