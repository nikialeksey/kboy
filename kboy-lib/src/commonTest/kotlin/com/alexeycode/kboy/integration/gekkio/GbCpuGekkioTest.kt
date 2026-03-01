package com.alexeycode.kboy.integration.gekkio

import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.serial.BufferSerial
import com.alexeycode.kboy.integration.TestsGb
import com.alexeycode.kboy.lib.Res
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
        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/test-roms/gekkio/acceptance/$gbFileName")
            )
        )
        val serial = BufferSerial()
        val gb = TestsGb(cartridge, serial)
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