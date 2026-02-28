package com.alexeycode.kboy.integration.gekkio

import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.serial.BufferSerial
import com.alexeycode.kboy.integration.TestsGb
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
        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/test-roms/gekkio/acceptance/$gbFileName")
            )
        )
        val serial = BufferSerial()
        val gb = TestsGb(cartridge, serial)
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