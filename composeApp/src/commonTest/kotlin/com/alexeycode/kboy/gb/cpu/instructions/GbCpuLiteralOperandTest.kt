package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80LiteralOperand
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.InMemoryRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.mem.GbMemory
import com.alexeycode.kboy.gb.ppu.GbLcdStatus
import com.alexeycode.kboy.gb.serial.BufferSerial
import kotlin.test.Test
import kotlin.test.assertEquals

class GbCpuLiteralOperandTest {

    @Test
    fun checkRead16() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val serial = BufferSerial()
        val lcdStatus = GbLcdStatus()
        val memory = GbMemory(
            interrupts,
            timer,
            serial,
            lcdStatus
        )
        assertEquals(
            0x38,
            Z80LiteralOperand("38H").read16(memory, InMemoryRegisters())
        )
    }
}