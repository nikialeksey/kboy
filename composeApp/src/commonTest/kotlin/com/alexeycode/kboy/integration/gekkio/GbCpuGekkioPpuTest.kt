package com.alexeycode.kboy.integration.gekkio

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.GbCpu
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.GbRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.joypad.GbJoypad
import com.alexeycode.kboy.gb.mem.GbBus
import com.alexeycode.kboy.gb.mem.GbDma
import com.alexeycode.kboy.gb.mem.GbDmaTransfer
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
import kotlin.test.assertTrue

private val SUCCESS_BYTES = byteArrayOf(3, 5, 8, 13, 21, 34)
private val FAILURE_BYTES = byteArrayOf(42, 42, 42, 42, 42, 42)

/**
 * Tests sources: https://github.com/Gekkio/mooneye-test-suite/
 */
@OptIn(ExperimentalResourceApi::class)
class GbCpuGekkioPpuTest {

    @Test
    fun `hblank_ly_scx_timing-GS`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/hblank_ly_scx_timing-GS.gb")
    }
    @Test
    fun `intr_1_2_timing-GS`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_1_2_timing-GS.gb")
    }
    @Test
    fun `intr_2_0_timing`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_2_0_timing.gb")
    }
    @Test
    fun `intr_2_mode0_timing`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_2_mode0_timing.gb")
    }
    @Test
    fun `intr_2_mode0_timing_sprites`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_2_mode0_timing_sprites.gb")
    }
    @Test
    fun `intr_2_mode3_timing`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_2_mode3_timing.gb")
    }
    @Test
    fun `intr_2_oam_ok_timing`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/intr_2_oam_ok_timing.gb")
    }
    @Test
    fun `lcdon_timing-GS`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/lcdon_timing-GS.gb")
    }
    @Test
    fun `lcdon_write_timing-GS`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/lcdon_write_timing-GS.gb")
    }
    @Test
    fun `stat_irq_blocking`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/stat_irq_blocking.gb")
    }
    @Test
    fun `stat_lyc_onoff`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/stat_lyc_onoff.gb")
    }
    @Test
    fun `vblank_stat_intr-GS`() = runTest {
        testGekkioCpuInstrsIndividual("ppu/vblank_stat_intr-GS.gb")
    }

    private suspend fun testGekkioCpuInstrsIndividual(gbFileName: String) {
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
                Res.readBytes("files/test-roms/gekkio/acceptance/$gbFileName")
            )
        )
        val memory = cartridge.memory()
        val bus = GbBus(
            memory,
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
        val dmaTransfer = GbDmaTransfer(memory, dma)

        val registers = GbRegisters()
        val cpu = GbCpu(
            r = registers,
            mem = bus,
            interrupts = interrupts
        )
        val gb = SimpleGb(
            timer = timer,
            cpu = cpu,
            dma = dmaTransfer,
            ppu = GbPpu(
                interrupts,
                memory,
                lcdStatus,
                lcdControl,
                palette,
                background,
                window
            )
        )
        for (i in 0 .. 100) {
            gb.run(100_000)
            if (isFinish(serial)) {
                break
            }
        }

        assertTrue(
            isSuccess(serial.asByteArray()),
            "Result: ${serial.asByteArray().asList()}"
        )
    }

    private fun isFinish(serial: BufferSerial): Boolean {
        val serialBytes = serial.asByteArray()
        val success = isSuccess(serialBytes)
        val fail = isFailure(serialBytes)
        return success || fail
    }

    private fun isSuccess(bytes: ByteArray): Boolean {
        return bytes.contentEquals(SUCCESS_BYTES)
    }

    private fun isFailure(bytes: ByteArray): Boolean {
        return bytes.contentEquals(FAILURE_BYTES)
    }
}