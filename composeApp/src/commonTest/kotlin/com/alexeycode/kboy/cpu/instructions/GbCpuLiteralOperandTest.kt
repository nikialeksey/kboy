package com.alexeycode.kboy.cpu.instructions

import com.alexeycode.kboy.cpu.instructions.operands.Z80LiteralOperand
import com.alexeycode.kboy.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.cpu.registers.InMemoryRegisters
import com.alexeycode.kboy.cpu.timer.GbTimer
import com.alexeycode.kboy.mem.GbMemory
import com.alexeycode.kboy.serial.BufferSerial
import kotlin.test.Test
import kotlin.test.assertEquals

class GbCpuLiteralOperandTest {

    @Test
    fun checkRead16() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val serial = BufferSerial()
        val memory = GbMemory(
            interrupts,
            timer, serial
        )
        assertEquals(
            0x38,
            Z80LiteralOperand("38H").read16(memory, InMemoryRegisters())
        )
    }
}