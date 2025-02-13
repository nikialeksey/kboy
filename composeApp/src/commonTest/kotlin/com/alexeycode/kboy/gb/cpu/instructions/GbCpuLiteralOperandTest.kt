package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80LiteralOperand
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.InMemoryRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.joypad.GbJoypad
import com.alexeycode.kboy.gb.mem.GbDma
import com.alexeycode.kboy.gb.mem.GbMemory
import com.alexeycode.kboy.gb.ppu.GbBackground
import com.alexeycode.kboy.gb.ppu.GbLcdControl
import com.alexeycode.kboy.gb.ppu.GbLcdStatus
import com.alexeycode.kboy.gb.ppu.GbPalette
import com.alexeycode.kboy.gb.ppu.GbWindow
import com.alexeycode.kboy.gb.serial.BufferSerial
import kotlin.test.Test
import kotlin.test.assertEquals

class GbCpuLiteralOperandTest {

    @Test
    fun checkRead16() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val dma = GbDma()
        val serial = BufferSerial()
        val joypad = GbJoypad(interrupts)
        val lcdStatus = GbLcdStatus()
        val lcdControl = GbLcdControl()
        val palette = GbPalette()
        val background = GbBackground()
        val window = GbWindow()

        val memory = GbMemory(
            interrupts,
            timer,
            dma,
            serial,
            joypad,
            lcdStatus,
            lcdControl,
            palette,
            background,
            window
        )
        assertEquals(
            0x38,
            Z80LiteralOperand("38H").read16(memory, InMemoryRegisters())
        )
    }
}