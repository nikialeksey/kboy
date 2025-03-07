package com.alexeycode.kboy.integration.micro

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
import kotlin.test.assertEquals

/**
 * Tests source: https://github.com/aappleby/gbmicrotest
 */
@OptIn(ExperimentalResourceApi::class)
class GbCpuGbMicroTimerTest {

    @Test
    fun `timer_div_phase_c`() =
        runTest { testGbMicrotest("timer_div_phase_c.gb") }

    @Test
    fun `timer_div_phase_d`() =
        runTest { testGbMicrotest("timer_div_phase_d.gb") }

    @Test
    fun `timer_tima_inc_64k_a`() =
        runTest { testGbMicrotest("timer_tima_inc_64k_a.gb") }

    @Test
    fun `timer_tima_inc_64k_b`() =
        runTest { testGbMicrotest("timer_tima_inc_64k_b.gb") }

    @Test
    fun `timer_tima_inc_64k_c`() =
        runTest { testGbMicrotest("timer_tima_inc_64k_c.gb") }

    @Test
    fun `timer_tima_inc_64k_d`() =
        runTest { testGbMicrotest("timer_tima_inc_64k_d.gb") }

    @Test
    fun `timer_tima_inc_256k_a`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_a.gb") }

    @Test
    fun `timer_tima_inc_256k_b`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_b.gb") }

    @Test
    fun `timer_tima_inc_256k_c`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_c.gb") }

    @Test
    fun `timer_tima_inc_256k_d`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_d.gb") }

    @Test
    fun `timer_tima_inc_256k_e`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_e.gb") }

    @Test
    fun `timer_tima_inc_256k_f`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_f.gb") }

    @Test
    fun `timer_tima_inc_256k_g`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_g.gb") }

    @Test
    fun `timer_tima_inc_256k_h`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_h.gb") }

    @Test
    fun `timer_tima_inc_256k_i`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_i.gb") }

    @Test
    fun `timer_tima_inc_256k_j`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_j.gb") }

    @Test
    fun `timer_tima_inc_256k_k`() =
        runTest { testGbMicrotest("timer_tima_inc_256k_k.gb") }

    @Test
    fun `timer_tima_phase_a`() =
        runTest { testGbMicrotest("timer_tima_phase_a.gb") }

    @Test
    fun `timer_tima_phase_b`() =
        runTest { testGbMicrotest("timer_tima_phase_b.gb") }

    @Test
    fun `timer_tima_phase_c`() =
        runTest { testGbMicrotest("timer_tima_phase_c.gb") }

    @Test
    fun `timer_tima_phase_d`() =
        runTest { testGbMicrotest("timer_tima_phase_d.gb") }

    @Test
    fun `timer_tima_phase_e`() =
        runTest { testGbMicrotest("timer_tima_phase_e.gb") }

    @Test
    fun `timer_tima_phase_f`() =
        runTest { testGbMicrotest("timer_tima_phase_f.gb") }

    @Test
    fun `timer_tima_phase_g`() =
        runTest { testGbMicrotest("timer_tima_phase_g.gb") }

    @Test
    fun `timer_tima_phase_h`() =
        runTest { testGbMicrotest("timer_tima_phase_h.gb") }

    @Test
    fun `timer_tima_phase_i`() =
        runTest { testGbMicrotest("timer_tima_phase_i.gb") }

    @Test
    fun `timer_tima_phase_j`() =
        runTest { testGbMicrotest("timer_tima_phase_j.gb") }

    @Test
    fun `timer_tima_reload_256k_a`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_a.gb") }

    @Test
    fun `timer_tima_reload_256k_b`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_b.gb") }

    @Test
    fun `timer_tima_reload_256k_c`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_c.gb") }

    @Test
    fun `timer_tima_reload_256k_d`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_d.gb") }

    @Test
    fun `timer_tima_reload_256k_e`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_e.gb") }

    @Test
    fun `timer_tima_reload_256k_f`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_f.gb") }

    @Test
    fun `timer_tima_reload_256k_g`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_g.gb") }

    @Test
    fun `timer_tima_reload_256k_h`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_h.gb") }

    @Test
    fun `timer_tima_reload_256k_i`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_i.gb") }

    @Test
    fun `timer_tima_reload_256k_j`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_j.gb") }

    @Test
    fun `timer_tima_reload_256k_k`() =
        runTest { testGbMicrotest("timer_tima_reload_256k_k.gb") }

    @Test
    fun `timer_tima_write_a`() =
        runTest { testGbMicrotest("timer_tima_write_a.gb") }

    @Test
    fun `timer_tima_write_b`() =
        runTest { testGbMicrotest("timer_tima_write_b.gb") }

    @Test
    fun `timer_tima_write_c`() =
        runTest { testGbMicrotest("timer_tima_write_c.gb") }

    @Test
    fun `timer_tima_write_d`() =
        runTest { testGbMicrotest("timer_tima_write_d.gb") }

    @Test
    fun `timer_tima_write_e`() =
        runTest { testGbMicrotest("timer_tima_write_e.gb") }

    @Test
    fun `timer_tima_write_f`() =
        runTest { testGbMicrotest("timer_tima_write_f.gb") }

    @Test
    fun `timer_tma_write_a`() =
        runTest { testGbMicrotest("timer_tma_write_a.gb") }

    @Test
    fun `timer_tma_write_b`() =
        runTest { testGbMicrotest("timer_tma_write_b.gb") }

    private suspend fun testGbMicrotest(gbFileName: String, ticksCount: Int = 1000) {
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
                Res.readBytes("files/test-roms/gbmicrotest/$gbFileName")
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
        gb.run(ticksCount)

        val result = bus.read8(0xFF82)
        assertEquals(0x01, result)
    }
}