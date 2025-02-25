package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.InMemoryRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.joypad.GbJoypad
import com.alexeycode.kboy.gb.mem.GbDma
import com.alexeycode.kboy.gb.mem.GbDmaTransfer
import com.alexeycode.kboy.gb.mem.GbMemory
import com.alexeycode.kboy.gb.ppu.GbBackground
import com.alexeycode.kboy.gb.ppu.GbLcdControl
import com.alexeycode.kboy.gb.ppu.GbLcdStatus
import com.alexeycode.kboy.gb.ppu.GbPalette
import com.alexeycode.kboy.gb.ppu.GbPpu
import com.alexeycode.kboy.gb.ppu.GbWindow
import com.alexeycode.kboy.gb.serial.BufferSerial
import kboy.composeapp.generated.resources.Res
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.test.Test
import kotlin.test.assertContains

/**
 * Tests source: https://github.com/L-P/blargg-test-roms
 */
@OptIn(ExperimentalResourceApi::class)
class GbCpuBlarggTest {

    @Test
    fun testCpuInstrs01() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/01-special.gb")
    }

    @Test
    fun testCpuInstrs02() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/02-interrupts.gb")
    }

    @Test
    fun testCpuInstrs03() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/03-op sp,hl.gb")
    }

    @Test
    fun testCpuInstrs04() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/04-op r,imm.gb")
    }

    @Test
    fun testCpuInstrs05() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/05-op rp.gb")
    }

    @Test
    fun testCpuInstrs06() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/06-ld r,r.gb")
    }

    @Test
    fun testCpuInstrs07() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/07-jr,jp,call,ret,rst.gb")
    }

    @Test
    fun testCpuInstrs08() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/08-misc instrs.gb")
    }

    @Test
    fun testCpuInstrs09() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/09-op r,r.gb")
    }

    @Test
    fun testCpuInstrs10() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/10-bit ops.gb")
    }

    @Test
    fun testCpuInstrs11() = runTest {
        testBlarggCpuInstrsIndividual("cpu-instrs/11-op a,(hl).gb")
    }

    @Test
    fun testInstrTiming() = runTest {
        testBlarggCpuInstrsIndividual("instr_timing/instr_timing.gb")
    }

    private suspend fun testBlarggCpuInstrsIndividual(gbFileName: String) {
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

        val ram = GbMemory(interrupts, timer, dma, serial, joypad, lcdStatus, lcdControl, palette, background, window)
        val dmaTransfer = GbDmaTransfer(ram, dma)
        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/test-roms/blargg/$gbFileName")
            )
        )
        cartridge.upload(ram)

        val registers = InMemoryRegisters()
        val cpu = GbCpu(
            r = registers,
            mem = ram,
            interrupts = interrupts
        )
        val gb = SimpleGb(
            timer = timer,
            cpu = cpu,
            dma = dmaTransfer,
            ppu = GbPpu(interrupts, ram, lcdStatus, lcdControl, palette, background, window)
        )
        while (true) {
            gb.run(100_000)
            val outputMessage = serial.asByteArray().decodeToString()
            if (outputMessage.contains("Passed") || outputMessage.contains("Failed")) {
                break
            }
        }

        val outputMessage = serial.asByteArray().decodeToString()
        assertContains(
            outputMessage,
            "Passed",
            message = "Actual output message: $outputMessage"
        )
    }
}