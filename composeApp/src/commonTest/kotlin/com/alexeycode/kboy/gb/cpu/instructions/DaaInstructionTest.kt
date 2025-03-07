package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.GbRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.joypad.GbJoypad
import com.alexeycode.kboy.gb.mem.GbDma
import com.alexeycode.kboy.gb.mem.GbBus
import com.alexeycode.kboy.gb.mem.SimpleMemory
import com.alexeycode.kboy.gb.ppu.GbBackground
import com.alexeycode.kboy.gb.ppu.GbLcdControl
import com.alexeycode.kboy.gb.ppu.GbLcdStatus
import com.alexeycode.kboy.gb.ppu.GbPalette
import com.alexeycode.kboy.gb.ppu.GbWindow
import com.alexeycode.kboy.gb.serial.BufferSerial
import kotlin.test.Test
import kotlin.test.assertEquals

class DaaInstructionTest {

    @Test
    fun plus_15_27_equals_42() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x15)
        registers.b().set(0x27)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x42, registers.a().get())
        assertEquals(0b0000_0000, registers.f().get())
    }

    @Test
    fun plus_9_3_equals_12() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x9)
        registers.b().set(0x3)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x12, registers.a().get())
        assertEquals(0b0000_0000, registers.f().get())
    }

    @Test
    fun plus_9_9_equals_18() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x9)
        registers.b().set(0x9)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x18, registers.a().get())
        assertEquals(0b0000_0000, registers.f().get())
    }

    @Test
    fun plus_29_19_equals_48() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x29)
        registers.b().set(0x19)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x48, registers.a().get())
        assertEquals(0b0000_0000, registers.f().get())
    }

    @Test
    fun plus_58_78_equals_12() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x58)
        registers.b().set(0x78)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x36, registers.a().get())
        assertEquals(0b0001_0000, registers.f().get())
    }

    @Test
    fun plus_88_99_equals_87() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x88)
        registers.b().set(0x99)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x87, registers.a().get())
        assertEquals(0b0001_0000, registers.f().get())
    }

    @Test
    fun plus_99_1_equals_00() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x99)
        registers.b().set(0x01)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x00, registers.a().get())
        assertEquals(0b1001_0000, registers.f().get())
    }

    @Test
    fun plus_0_0_equals_00() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x00)
        registers.b().set(0x00)
        alu8Instruction.execute(0x80) // a = a + b
        misc.execute(0x27)

        assertEquals(0x00, registers.a().get())
        assertEquals(0b1000_0000, registers.f().get())
    }

    @Test
    fun minus_45_12_equals_33() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x45)
        registers.b().set(0x12)
        alu8Instruction.execute(0x90) // a = a - b
        misc.execute(0x27)

        assertEquals(0x33, registers.a().get())
        assertEquals(0b0100_0000, registers.f().get())
    }

    @Test
    fun minus_90_10_equals_80() {
        val (interrupts, memory, registers) = prepareEnv()
        val alu8Instruction = Alu8Instruction(registers, memory)
        val misc = MiscInstruction(registers, memory, interrupts)

        registers.a().set(0x90)
        registers.b().set(0x10)
        alu8Instruction.execute(0x90) // a = a - b
        misc.execute(0x27)

        assertEquals(0x80, registers.a().get())
        assertEquals(0b0100_0000, registers.f().get())
    }

    private fun prepareEnv(): Triple<GbInterrupts, GbBus, GbRegisters> {
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
        val memory = GbBus(
            SimpleMemory(Array(0) { 0 }),
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
        val registers = GbRegisters()
        return Triple(interrupts, memory, registers)
    }
}