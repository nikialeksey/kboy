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
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

private val SUCCESS_BYTES = byteArrayOf(3, 5, 8, 13, 21, 34)
private val FAILURE_BYTES = byteArrayOf(42, 42, 42, 42, 42, 42)

/**
 * Tests sources: https://github.com/Gekkio/mooneye-test-suite/
 */
@OptIn(ExperimentalResourceApi::class)
class GbCpuGekkioTest {

    @Test
    fun testMemOam() = runTest {
        testGekkioCpuInstrsIndividual("bits/mem_oam.gb")
    }

    @Test
    fun testRegF() = runTest {
        testGekkioCpuInstrsIndividual("bits/reg_f.gb")
    }

    @Test
    fun testDaa() = runTest {
        testGekkioCpuInstrsIndividual("instr/daa.gb")
    }

    @Test
    @Ignore // TODO needs to investigate
    fun testIePush() = runTest {
        testGekkioCpuInstrsIndividual("interrupts/ie_push.gb")
    }

    @Test
    fun testOamDmaBasic() = runTest {
        testGekkioCpuInstrsIndividual("oam_dma/basic.gb")
    }

    @Test
    fun testOamDmaRegRead() = runTest {
        testGekkioCpuInstrsIndividual("oam_dma/reg_read.gb")
    }

    @Test
    fun testPpu() = runTest {
        // TODO all of comment tests are freezing
//        testGekkioCpuInstrsIndividual("ppu/hblank_ly_scx_timing-GS.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_1_2_timing-GS.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_2_0_timing.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_2_mode0_timing.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_2_mode0_timing_sprites.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_2_mode3_timing.gb")
//        testGekkioCpuInstrsIndividual("ppu/intr_2_oam_ok_timing.gb")
//        testGekkioCpuInstrsIndividual("ppu/lcdon_timing-GS.gb")
//        testGekkioCpuInstrsIndividual("ppu/lcdon_write_timing-GS.gb")
//        testGekkioCpuInstrsIndividual("ppu/stat_irq_blocking.gb")
//        testGekkioCpuInstrsIndividual("ppu/stat_lyc_onoff.gb")
        testGekkioCpuInstrsIndividual("ppu/vblank_stat_intr-GS.gb")
    }

    @Test
    @Ignore // TODO freezing
    fun testSerial() = runTest {
        testGekkioCpuInstrsIndividual("serial/boot_sclk_align-dmgABCmgb.gb")
    }

    @Test
    @Ignore // TODO Requires MBC to be emulated
    fun testOamDmaSourcesGs() = runTest {
        testGekkioCpuInstrsIndividual("oam_dma/sources-GS.gb")
    }

    @Test
    fun testTimer() = runTest {
        // TODO commented tests are freezing
        testGekkioCpuInstrsIndividual("timer/div_write.gb")
//        testGekkioCpuInstrsIndividual("timer/rapid_toggle.gb")
        testGekkioCpuInstrsIndividual("timer/tim00.gb")
//        testGekkioCpuInstrsIndividual("timer/tim00_div_trigger.gb")
        testGekkioCpuInstrsIndividual("timer/tim01.gb")
//        testGekkioCpuInstrsIndividual("timer/tim01_div_trigger.gb")
        testGekkioCpuInstrsIndividual("timer/tim10.gb")
//        testGekkioCpuInstrsIndividual("timer/tim10_div_trigger.gb")
        testGekkioCpuInstrsIndividual("timer/tim11.gb")
//        testGekkioCpuInstrsIndividual("timer/tim11_div_trigger.gb")
        testGekkioCpuInstrsIndividual("timer/tima_reload.gb")
//        testGekkioCpuInstrsIndividual("timer/tima_write_reloading.gb")
//        testGekkioCpuInstrsIndividual("timer/tma_write_reloading.gb")
    }

    @Test
    fun testAcceptance() = runTest {
        // TODO commented tests are freezing
//        testGekkioCpuInstrsIndividual("add_sp_e_timing.gb")
//        testGekkioCpuInstrsIndividual("boot_div-dmg0.gb")
//        testGekkioCpuInstrsIndividual("boot_div-dmgABCmgb.gb")
//        testGekkioCpuInstrsIndividual("boot_hwio-dmg0.gb")
//        testGekkioCpuInstrsIndividual("boot_hwio-dmgABCmgb.gb")
//        testGekkioCpuInstrsIndividual("boot_regs-dmg0.gb")
        testGekkioCpuInstrsIndividual("boot_regs-dmgABC.gb")
//        testGekkioCpuInstrsIndividual("call_cc_timing.gb")
//        testGekkioCpuInstrsIndividual("call_cc_timing2.gb")
//        testGekkioCpuInstrsIndividual("call_timing.gb")
//        testGekkioCpuInstrsIndividual("call_timing2.gb")
//        testGekkioCpuInstrsIndividual("di_timing-GS.gb")
        testGekkioCpuInstrsIndividual("div_timing.gb")
//        testGekkioCpuInstrsIndividual("ei_sequence.gb")
        testGekkioCpuInstrsIndividual("ei_timing.gb")
        testGekkioCpuInstrsIndividual("halt_ime0_ei.gb")
//        testGekkioCpuInstrsIndividual("halt_ime0_nointr_timing.gb")
        testGekkioCpuInstrsIndividual("halt_ime1_timing.gb")
//        testGekkioCpuInstrsIndividual("halt_ime1_timing2-GS.gb")
        testGekkioCpuInstrsIndividual("if_ie_registers.gb")
//        testGekkioCpuInstrsIndividual("intr_timing.gb")
//        testGekkioCpuInstrsIndividual("jp_cc_timing.gb")
//        testGekkioCpuInstrsIndividual("jp_timing.gb")
//        testGekkioCpuInstrsIndividual("ld_hl_sp_e_timing.gb")
//        testGekkioCpuInstrsIndividual("oam_dma_restart.gb")
//        testGekkioCpuInstrsIndividual("oam_dma_start.gb")
//        testGekkioCpuInstrsIndividual("oam_dma_timing.gb")
//        testGekkioCpuInstrsIndividual("pop_timing.gb")
//        testGekkioCpuInstrsIndividual("push_timing.gb")
        testGekkioCpuInstrsIndividual("rapid_di_ei.gb")
//        testGekkioCpuInstrsIndividual("ret_cc_timing.gb") TODO this test fails
//        testGekkioCpuInstrsIndividual("ret_timing.gb") TODO this test fails
//        testGekkioCpuInstrsIndividual("reti_intr_timing.gb")
//        testGekkioCpuInstrsIndividual("reti_timing.gb") TODO this test fails
//        testGekkioCpuInstrsIndividual("rst_timing.gb")
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

        val ram = GbMemory(interrupts, timer, dma, serial, joypad, lcdStatus, lcdControl, palette, background, window)
        val dmaTransfer = GbDmaTransfer(ram, dma)
        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/test-roms/gekkio/acceptance/$gbFileName")
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
            if (isFinish(serial)) {
                break
            }
        }

        assertTrue(isSuccess(serial.asByteArray()))
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