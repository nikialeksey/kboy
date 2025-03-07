package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.GbRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.joypad.GbJoypad
import com.alexeycode.kboy.gb.mem.GbDma
import com.alexeycode.kboy.gb.mem.GbDmaTransfer
import com.alexeycode.kboy.gb.mem.GbBus
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

@OptIn(ExperimentalResourceApi::class)
class DesktopGbCpuTest {

    @Test
    fun test01() = runTest {
        testCpuInstrsIndividual("cpu-instrs/01-special.gb")
    }

    @Test
    fun test02() = runTest {
        testCpuInstrsIndividual("cpu-instrs/02-interrupts.gb")
    }

    @Test
    fun test03() = runTest {
        testCpuInstrsIndividual("cpu-instrs/03-op sp,hl.gb")
    }

    @Test
    fun test04() = runTest {
        testCpuInstrsIndividual("cpu-instrs/04-op r,imm.gb")
    }

    @Test
    fun test05() = runTest {
        testCpuInstrsIndividual("cpu-instrs/05-op rp.gb")
    }

    @Test
    fun test06() = runTest {
        testCpuInstrsIndividual("cpu-instrs/06-ld r,r.gb")
    }

    @Test
    fun test07() = runTest {
        testCpuInstrsIndividual("cpu-instrs/07-jr,jp,call,ret,rst.gb")
    }

    @Test
    fun test08() = runTest {
        testCpuInstrsIndividual("cpu-instrs/08-misc instrs.gb")
    }

    @Test
    fun test09() = runTest {
        testCpuInstrsIndividual("cpu-instrs/09-op r,r.gb")
    }

    @Test
    fun test10() = runTest {
        testCpuInstrsIndividual("cpu-instrs/10-bit ops.gb")
    }

    @Test
    fun test11() = runTest {
        testCpuInstrsIndividual("cpu-instrs/11-op a,(hl).gb")
    }

    private suspend fun testCpuInstrsIndividual(gbFileName: String) {
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

        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/$gbFileName")
            )
        )
        val memory = cartridge.memory()
        val bus = GbBus(memory, interrupts, timer, dma, serial, joypad, lcdStatus, lcdControl, palette, background, window)
        val dmaTransfer = GbDmaTransfer(memory, dma)

        val registers = GbRegisters()
        val cpu = GbCpu(
            r = registers,
            mem = bus,
            interrupts = interrupts
        )
        val gb = SimpleGb(
            timer = timer,
            cpu = DoctorGbCpu(TraceableCpu(cpu, registers, bus, traceSize = 1)),
            dma = dmaTransfer,
            ppu = GbPpu(interrupts, memory, lcdStatus, lcdControl, palette, background, window)
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