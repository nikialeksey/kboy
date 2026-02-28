package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class RestartsInstruction(
    private val r: Registers,
    private val mem: Memory,
) : Instruction {

    override fun execute(opcode: Int): Int {
        return when (opcode) {
            // Restarts
            0xC7 -> restartTo({ 0x00 })
            0xCF -> restartTo({ 0x08 })
            0xD7 -> restartTo({ 0x10 })
            0xDF -> restartTo({ 0x18 })
            0xE7 -> restartTo({ 0x20 })
            0xEF -> restartTo({ 0x28 })
            0xF7 -> restartTo({ 0x30 })
            0xFF -> restartTo({ 0x38 })
            else -> {
                0
            }
        }
    }

    private inline fun restartTo(address: () -> Int): Int {
        val a = address()

        val pc = r.pc().get()
        mem.write8(r.sp().get() - 1, pc.shr(8).and(0xFF))
        mem.write8(r.sp().get() - 2, pc.and(0xFF))
        r.sp().set(r.sp().get() - 2)
        r.pc().set(a)

        return 16
    }
}