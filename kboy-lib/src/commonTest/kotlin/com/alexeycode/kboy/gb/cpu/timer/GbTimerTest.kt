package com.alexeycode.kboy.gb.cpu.timer

import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import kotlin.test.Test
import kotlin.test.assertEquals

class GbTimerTest {
    @Test
    fun incrementEvery16Ticks() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0xFF,
            tac = 0b101, // tima will increment every 16 ticks
            tima = 0x00,
        )

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(0, timer.div())

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(0, timer.div())

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(0, timer.div())

        timer.tick(4)
        assertEquals(1, timer.tima())
        assertEquals(0, timer.div())
    }

    @Test
    fun incrementEvery64Ticks() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0xFF,
            tac = 0b110,
            tima = 0x00,
        )

        repeat(15) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(0, timer.div())
        }

        timer.tick(4)
        assertEquals(1, timer.tima())
        assertEquals(0, timer.div())
    }

    @Test
    fun incrementEvery256Ticks() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0xFF,
            tac = 0b111,
            tima = 0x00,
        )

        repeat(63) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(0, timer.div())
        }

        timer.tick(4)
        assertEquals(1, timer.tima())
        assertEquals(1, timer.div())
    }

    @Test
    fun incrementEvery1024Ticks() {
        val interrupts = GbInterrupts()
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0xFF,
            tac = 0b100,
            tima = 0x00,
        )

        repeat(63) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(0, timer.div())
        }

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(1, timer.div())

        // 256 cycles done above

        repeat(63) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(1, timer.div())
        }

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(2, timer.div())

        // 512 cycles done above

        repeat(63) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(2, timer.div())
        }

        timer.tick(4)
        assertEquals(0, timer.tima())
        assertEquals(3, timer.div())

        // 512+256 cycles done above

        repeat(63) {
            timer.tick(4)
            assertEquals(0, timer.tima())
            assertEquals(3, timer.div())
        }

        timer.tick(4)
        assertEquals(1, timer.tima())
        assertEquals(4, timer.div())
    }

    @Test
    fun incrementEvery16TicksAndOverflow() {
        val interrupts = GbInterrupts(ifFlag = 0xE0)
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0xFF,
            tac = 0b101, // tima will increment every 16 ticks
            tima = 0xFF,
        )

        repeat(3) {
            timer.tick(4)
            assertEquals(0xFF, timer.tima())
        }

        timer.tick(4)
        assertEquals(0, timer.tima())

        // overflow should start
        assertEquals(0xE0, interrupts.ifFlag())

        timer.tick(4)
        assertEquals(0xE4, interrupts.ifFlag())
        assertEquals(0xFF, timer.tima())
    }

    @Test
    fun incrementEvery16TicksAndCompleteFullTimaCycleToOverflow() {
        val interrupts = GbInterrupts(ifFlag = 0x00)
        val timer = GbTimer(
            interrupts,
            div = 0x00,
            tma = 0x00,
            tac = 0x05, // tima will increment every 16 ticks
            tima = 0x00,
        )

        repeat(500) {
            timer.tick(4)
        }

        repeat(500) {
            timer.tick(4)
        }

        assertEquals(0x00, interrupts.ifFlag())

        repeat(500) {
            timer.tick(4)
        }

        assertEquals(0x04, interrupts.ifFlag())
    }
}