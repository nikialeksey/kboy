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
import com.alexeycode.kboy.lib.Res
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests source: https://github.com/aappleby/gbmicrotest
 */
@OptIn(ExperimentalResourceApi::class)
@Ignore
class GbCpuGbMicroTest {

    @Test
    fun `000-oam_lock`() = runTest { testGbMicrotest("000-oam_lock.gb") }

    @Test
    fun `000-write_to_x8000`() =
        runTest { testGbMicrotest("000-write_to_x8000.gb") }

    @Test
    fun `001-vram_unlocked`() =
        runTest { testGbMicrotest("001-vram_unlocked.gb") }

    @Test
    fun `002-vram_locked`() = runTest { testGbMicrotest("002-vram_locked.gb") }

    @Test
    fun `004-tima_boot_phase`() =
        runTest { testGbMicrotest("004-tima_boot_phase.gb") }

    @Test
    fun `004-tima_cycle_timer`() =
        runTest { testGbMicrotest("004-tima_cycle_timer.gb") }

    @Test
    fun `007-lcd_on_stat`() = runTest { testGbMicrotest("007-lcd_on_stat.gb") }

    @Test
    fun `400-dma`() = runTest { testGbMicrotest("400-dma.gb") }

    @Test
    fun `500-scx-timing`() = runTest { testGbMicrotest("500-scx-timing.gb") }

    @Test
    fun `800-ppu-latch-scx`() =
        runTest { testGbMicrotest("800-ppu-latch-scx.gb") }

    @Test
    fun `801-ppu-latch-scy`() =
        runTest { testGbMicrotest("801-ppu-latch-scy.gb") }

    @Test
    fun `802-ppu-latch-tileselect`() =
        runTest { testGbMicrotest("802-ppu-latch-tileselect.gb") }

    @Test
    fun `803-ppu-latch-bgdisplay`() =
        runTest { testGbMicrotest("803-ppu-latch-bgdisplay.gb") }

    @Test
    fun `audio_testbench`() = runTest { testGbMicrotest("audio_testbench.gb") }

    @Test
    fun `cpu_bus_1`() = runTest { testGbMicrotest("cpu_bus_1.gb") }

    @Test
    fun `div_inc_timing_a`() =
        runTest { testGbMicrotest("div_inc_timing_a.gb") }

    @Test
    fun `div_inc_timing_b`() =
        runTest { testGbMicrotest("div_inc_timing_b.gb") }

    @Test
    fun `dma_0x1000`() = runTest { testGbMicrotest("dma_0x1000.gb") }

    @Test
    fun `dma_0x9000`() = runTest { testGbMicrotest("dma_0x9000.gb") }

    @Test
    fun `dma_0xA000`() = runTest { testGbMicrotest("dma_0xA000.gb") }

    @Test
    fun `dma_0xC000`() = runTest { testGbMicrotest("dma_0xC000.gb") }

    @Test
    fun `dma_0xE000`() = runTest { testGbMicrotest("dma_0xE000.gb") }

    @Test
    fun `dma_basic`() = runTest { testGbMicrotest("dma_basic.gb") }

    @Test
    fun `dma_timing_a`() = runTest { testGbMicrotest("dma_timing_a.gb") }

    @Test
    fun `flood_vram`() = runTest { testGbMicrotest("flood_vram.gb") }

    @Test
    fun `halt_bug`() = runTest { testGbMicrotest("halt_bug.gb") }

    @Test
    fun `halt_op_dupe`() = runTest { testGbMicrotest("halt_op_dupe.gb") }

    @Test
    fun `halt_op_dupe_delay`() =
        runTest { testGbMicrotest("halt_op_dupe_delay.gb") }

    @Test
    fun `hblank_int_di_timing_a`() =
        runTest { testGbMicrotest("hblank_int_di_timing_a.gb") }

    @Test
    fun `hblank_int_di_timing_b`() =
        runTest { testGbMicrotest("hblank_int_di_timing_b.gb") }

    @Test
    fun `hblank_int_if_a`() = runTest { testGbMicrotest("hblank_int_if_a.gb") }

    @Test
    fun `hblank_int_if_b`() = runTest { testGbMicrotest("hblank_int_if_b.gb") }

    @Test
    fun `hblank_int_l0`() = runTest { testGbMicrotest("hblank_int_l0.gb") }

    @Test
    fun `hblank_int_l1`() = runTest { testGbMicrotest("hblank_int_l1.gb") }

    @Test
    fun `hblank_int_l2`() = runTest { testGbMicrotest("hblank_int_l2.gb") }

    @Test
    fun `hblank_int_scx0`() = runTest { testGbMicrotest("hblank_int_scx0.gb") }

    @Test
    fun `hblank_int_scx0_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx0_if_a.gb") }

    @Test
    fun `hblank_int_scx0_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx0_if_b.gb") }

    @Test
    fun `hblank_int_scx0_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx0_if_c.gb") }

    @Test
    fun `hblank_int_scx0_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx0_if_d.gb") }

    @Test
    fun `hblank_int_scx1`() = runTest { testGbMicrotest("hblank_int_scx1.gb") }

    @Test
    fun `hblank_int_scx1_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx1_if_a.gb") }

    @Test
    fun `hblank_int_scx1_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx1_if_b.gb") }

    @Test
    fun `hblank_int_scx1_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx1_if_c.gb") }

    @Test
    fun `hblank_int_scx1_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx1_if_d.gb") }

    @Test
    fun `hblank_int_scx1_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx1_nops_a.gb") }

    @Test
    fun `hblank_int_scx1_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx1_nops_b.gb") }

    @Test
    fun `hblank_int_scx2`() = runTest { testGbMicrotest("hblank_int_scx2.gb") }

    @Test
    fun `hblank_int_scx2_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx2_if_a.gb") }

    @Test
    fun `hblank_int_scx2_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx2_if_b.gb") }

    @Test
    fun `hblank_int_scx2_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx2_if_c.gb") }

    @Test
    fun `hblank_int_scx2_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx2_if_d.gb") }

    @Test
    fun `hblank_int_scx2_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx2_nops_a.gb") }

    @Test
    fun `hblank_int_scx2_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx2_nops_b.gb") }

    @Test
    fun `hblank_int_scx3`() = runTest { testGbMicrotest("hblank_int_scx3.gb") }

    @Test
    fun `hblank_int_scx3_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx3_if_a.gb") }

    @Test
    fun `hblank_int_scx3_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx3_if_b.gb") }

    @Test
    fun `hblank_int_scx3_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx3_if_c.gb") }

    @Test
    fun `hblank_int_scx3_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx3_if_d.gb") }

    @Test
    fun `hblank_int_scx3_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx3_nops_a.gb") }

    @Test
    fun `hblank_int_scx3_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx3_nops_b.gb") }

    @Test
    fun `hblank_int_scx4`() = runTest { testGbMicrotest("hblank_int_scx4.gb") }

    @Test
    fun `hblank_int_scx4_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx4_if_a.gb") }

    @Test
    fun `hblank_int_scx4_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx4_if_b.gb") }

    @Test
    fun `hblank_int_scx4_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx4_if_c.gb") }

    @Test
    fun `hblank_int_scx4_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx4_if_d.gb") }

    @Test
    fun `hblank_int_scx4_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx4_nops_a.gb") }

    @Test
    fun `hblank_int_scx4_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx4_nops_b.gb") }

    @Test
    fun `hblank_int_scx5`() = runTest { testGbMicrotest("hblank_int_scx5.gb") }

    @Test
    fun `hblank_int_scx5_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx5_if_a.gb") }

    @Test
    fun `hblank_int_scx5_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx5_if_b.gb") }

    @Test
    fun `hblank_int_scx5_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx5_if_c.gb") }

    @Test
    fun `hblank_int_scx5_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx5_if_d.gb") }

    @Test
    fun `hblank_int_scx5_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx5_nops_a.gb") }

    @Test
    fun `hblank_int_scx5_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx5_nops_b.gb") }

    @Test
    fun `hblank_int_scx6`() = runTest { testGbMicrotest("hblank_int_scx6.gb") }

    @Test
    fun `hblank_int_scx6_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx6_if_a.gb") }

    @Test
    fun `hblank_int_scx6_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx6_if_b.gb") }

    @Test
    fun `hblank_int_scx6_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx6_if_c.gb") }

    @Test
    fun `hblank_int_scx6_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx6_if_d.gb") }

    @Test
    fun `hblank_int_scx6_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx6_nops_a.gb") }

    @Test
    fun `hblank_int_scx6_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx6_nops_b.gb") }

    @Test
    fun `hblank_int_scx7`() = runTest { testGbMicrotest("hblank_int_scx7.gb") }

    @Test
    fun `hblank_int_scx7_if_a`() =
        runTest { testGbMicrotest("hblank_int_scx7_if_a.gb") }

    @Test
    fun `hblank_int_scx7_if_b`() =
        runTest { testGbMicrotest("hblank_int_scx7_if_b.gb") }

    @Test
    fun `hblank_int_scx7_if_c`() =
        runTest { testGbMicrotest("hblank_int_scx7_if_c.gb") }

    @Test
    fun `hblank_int_scx7_if_d`() =
        runTest { testGbMicrotest("hblank_int_scx7_if_d.gb") }

    @Test
    fun `hblank_int_scx7_nops_a`() =
        runTest { testGbMicrotest("hblank_int_scx7_nops_a.gb") }

    @Test
    fun `hblank_int_scx7_nops_b`() =
        runTest { testGbMicrotest("hblank_int_scx7_nops_b.gb") }

    @Test
    fun `hblank_scx2_if_a`() =
        runTest { testGbMicrotest("hblank_scx2_if_a.gb") }

    @Test
    fun `hblank_scx3_if_a`() =
        runTest { testGbMicrotest("hblank_scx3_if_a.gb") }

    @Test
    fun `hblank_scx3_if_b`() =
        runTest { testGbMicrotest("hblank_scx3_if_b.gb") }

    @Test
    fun `hblank_scx3_if_c`() =
        runTest { testGbMicrotest("hblank_scx3_if_c.gb") }

    @Test
    fun `hblank_scx3_if_d`() =
        runTest { testGbMicrotest("hblank_scx3_if_d.gb") }

    @Test
    fun `hblank_scx3_int_a`() =
        runTest { testGbMicrotest("hblank_scx3_int_a.gb") }

    @Test
    fun `hblank_scx3_int_b`() =
        runTest { testGbMicrotest("hblank_scx3_int_b.gb") }

    @Test
    fun `int_hblank_halt_bug_a`() =
        runTest { testGbMicrotest("int_hblank_halt_bug_a.gb") }

    @Test
    fun `int_hblank_halt_bug_b`() =
        runTest { testGbMicrotest("int_hblank_halt_bug_b.gb") }

    @Test
    fun `int_hblank_halt_scx0`() =
        runTest { testGbMicrotest("int_hblank_halt_scx0.gb") }

    @Test
    fun `int_hblank_halt_scx1`() =
        runTest { testGbMicrotest("int_hblank_halt_scx1.gb") }

    @Test
    fun `int_hblank_halt_scx2`() =
        runTest { testGbMicrotest("int_hblank_halt_scx2.gb") }

    @Test
    fun `int_hblank_halt_scx3`() =
        runTest { testGbMicrotest("int_hblank_halt_scx3.gb") }

    @Test
    fun `int_hblank_halt_scx4`() =
        runTest { testGbMicrotest("int_hblank_halt_scx4.gb") }

    @Test
    fun `int_hblank_halt_scx5`() =
        runTest { testGbMicrotest("int_hblank_halt_scx5.gb") }

    @Test
    fun `int_hblank_halt_scx6`() =
        runTest { testGbMicrotest("int_hblank_halt_scx6.gb") }

    @Test
    fun `int_hblank_halt_scx7`() =
        runTest { testGbMicrotest("int_hblank_halt_scx7.gb") }

    @Test
    fun `int_hblank_incs_scx0`() =
        runTest { testGbMicrotest("int_hblank_incs_scx0.gb") }

    @Test
    fun `int_hblank_incs_scx1`() =
        runTest { testGbMicrotest("int_hblank_incs_scx1.gb") }

    @Test
    fun `int_hblank_incs_scx2`() =
        runTest { testGbMicrotest("int_hblank_incs_scx2.gb") }

    @Test
    fun `int_hblank_incs_scx3`() =
        runTest { testGbMicrotest("int_hblank_incs_scx3.gb") }

    @Test
    fun `int_hblank_incs_scx4`() =
        runTest { testGbMicrotest("int_hblank_incs_scx4.gb") }

    @Test
    fun `int_hblank_incs_scx5`() =
        runTest { testGbMicrotest("int_hblank_incs_scx5.gb") }

    @Test
    fun `int_hblank_incs_scx6`() =
        runTest { testGbMicrotest("int_hblank_incs_scx6.gb") }

    @Test
    fun `int_hblank_incs_scx7`() =
        runTest { testGbMicrotest("int_hblank_incs_scx7.gb") }

    @Test
    fun `int_hblank_nops_scx0`() =
        runTest { testGbMicrotest("int_hblank_nops_scx0.gb") }

    @Test
    fun `int_hblank_nops_scx1`() =
        runTest { testGbMicrotest("int_hblank_nops_scx1.gb") }

    @Test
    fun `int_hblank_nops_scx2`() =
        runTest { testGbMicrotest("int_hblank_nops_scx2.gb") }

    @Test
    fun `int_hblank_nops_scx3`() =
        runTest { testGbMicrotest("int_hblank_nops_scx3.gb") }

    @Test
    fun `int_hblank_nops_scx4`() =
        runTest { testGbMicrotest("int_hblank_nops_scx4.gb") }

    @Test
    fun `int_hblank_nops_scx5`() =
        runTest { testGbMicrotest("int_hblank_nops_scx5.gb") }

    @Test
    fun `int_hblank_nops_scx6`() =
        runTest { testGbMicrotest("int_hblank_nops_scx6.gb") }

    @Test
    fun `int_hblank_nops_scx7`() =
        runTest { testGbMicrotest("int_hblank_nops_scx7.gb") }

    @Test
    fun `int_lyc_halt`() = runTest { testGbMicrotest("int_lyc_halt.gb") }

    @Test
    fun `int_lyc_incs`() = runTest { testGbMicrotest("int_lyc_incs.gb") }

    @Test
    fun `int_lyc_nops`() = runTest { testGbMicrotest("int_lyc_nops.gb") }

    @Test
    fun `int_oam_halt`() = runTest { testGbMicrotest("int_oam_halt.gb") }

    @Test
    fun `int_oam_incs`() = runTest { testGbMicrotest("int_oam_incs.gb") }

    @Test
    fun `int_oam_nops`() = runTest { testGbMicrotest("int_oam_nops.gb") }

    @Test
    fun `int_timer_halt`() = runTest { testGbMicrotest("int_timer_halt.gb") }

    @Test
    fun `int_timer_halt_div_a`() =
        runTest { testGbMicrotest("int_timer_halt_div_a.gb") }

    @Test
    fun `int_timer_halt_div_b`() =
        runTest { testGbMicrotest("int_timer_halt_div_b.gb") }

    @Test
    fun `int_timer_incs`() = runTest { testGbMicrotest("int_timer_incs.gb") }

    @Test
    fun `int_timer_nops`() = runTest { testGbMicrotest("int_timer_nops.gb") }

    @Test
    fun `int_timer_nops_div_a`() =
        runTest { testGbMicrotest("int_timer_nops_div_a.gb") }

    @Test
    fun `int_timer_nops_div_b`() =
        runTest { testGbMicrotest("int_timer_nops_div_b.gb") }

    @Test
    fun `int_vblank1_halt`() =
        runTest { testGbMicrotest("int_vblank1_halt.gb") }

    @Test
    fun `int_vblank1_incs`() =
        runTest { testGbMicrotest("int_vblank1_incs.gb") }

    @Test
    fun `int_vblank1_nops`() =
        runTest { testGbMicrotest("int_vblank1_nops.gb") }

    @Test
    fun `int_vblank2_halt`() =
        runTest { testGbMicrotest("int_vblank2_halt.gb") }

    @Test
    fun `int_vblank2_incs`() =
        runTest { testGbMicrotest("int_vblank2_incs.gb") }

    @Test
    fun `int_vblank2_nops`() =
        runTest { testGbMicrotest("int_vblank2_nops.gb") }

    @Test
    fun `is_if_set_during_ime0`() =
        runTest { testGbMicrotest("is_if_set_during_ime0.gb") }

    @Test
    fun `lcdon_halt_to_vblank_int_a`() =
        runTest { testGbMicrotest("lcdon_halt_to_vblank_int_a.gb") }

    @Test
    fun `lcdon_halt_to_vblank_int_b`() =
        runTest { testGbMicrotest("lcdon_halt_to_vblank_int_b.gb") }

    @Test
    fun `lcdon_nops_to_vblank_int_a`() =
        runTest { testGbMicrotest("lcdon_nops_to_vblank_int_a.gb") }

    @Test
    fun `lcdon_nops_to_vblank_int_b`() =
        runTest { testGbMicrotest("lcdon_nops_to_vblank_int_b.gb") }

    @Test
    fun `lcdon_to_if_oam_a`() =
        runTest { testGbMicrotest("lcdon_to_if_oam_a.gb") }

    @Test
    fun `lcdon_to_if_oam_b`() =
        runTest { testGbMicrotest("lcdon_to_if_oam_b.gb") }

    @Test
    fun `lcdon_to_ly1_a`() = runTest { testGbMicrotest("lcdon_to_ly1_a.gb") }

    @Test
    fun `lcdon_to_ly1_b`() = runTest { testGbMicrotest("lcdon_to_ly1_b.gb") }

    @Test
    fun `lcdon_to_ly2_a`() = runTest { testGbMicrotest("lcdon_to_ly2_a.gb") }

    @Test
    fun `lcdon_to_ly2_b`() = runTest { testGbMicrotest("lcdon_to_ly2_b.gb") }

    @Test
    fun `lcdon_to_ly3_a`() = runTest { testGbMicrotest("lcdon_to_ly3_a.gb") }

    @Test
    fun `lcdon_to_ly3_b`() = runTest { testGbMicrotest("lcdon_to_ly3_b.gb") }

    @Test
    fun `lcdon_to_lyc1_int`() =
        runTest { testGbMicrotest("lcdon_to_lyc1_int.gb") }

    @Test
    fun `lcdon_to_lyc2_int`() =
        runTest { testGbMicrotest("lcdon_to_lyc2_int.gb") }

    @Test
    fun `lcdon_to_lyc3_int`() =
        runTest { testGbMicrotest("lcdon_to_lyc3_int.gb") }

    @Test
    fun `lcdon_to_oam_int_l0`() =
        runTest { testGbMicrotest("lcdon_to_oam_int_l0.gb") }

    @Test
    fun `lcdon_to_oam_int_l1`() =
        runTest { testGbMicrotest("lcdon_to_oam_int_l1.gb") }

    @Test
    fun `lcdon_to_oam_int_l2`() =
        runTest { testGbMicrotest("lcdon_to_oam_int_l2.gb") }

    @Test
    fun `lcdon_to_oam_unlock_a`() =
        runTest { testGbMicrotest("lcdon_to_oam_unlock_a.gb") }

    @Test
    fun `lcdon_to_oam_unlock_b`() =
        runTest { testGbMicrotest("lcdon_to_oam_unlock_b.gb") }

    @Test
    fun `lcdon_to_oam_unlock_c`() =
        runTest { testGbMicrotest("lcdon_to_oam_unlock_c.gb") }

    @Test
    fun `lcdon_to_oam_unlock_d`() =
        runTest { testGbMicrotest("lcdon_to_oam_unlock_d.gb") }

    @Test
    fun `lcdon_to_stat0_a`() =
        runTest { testGbMicrotest("lcdon_to_stat0_a.gb") }

    @Test
    fun `lcdon_to_stat0_b`() =
        runTest { testGbMicrotest("lcdon_to_stat0_b.gb") }

    @Test
    fun `lcdon_to_stat0_c`() =
        runTest { testGbMicrotest("lcdon_to_stat0_c.gb") }

    @Test
    fun `lcdon_to_stat0_d`() =
        runTest { testGbMicrotest("lcdon_to_stat0_d.gb") }

    @Test
    fun `lcdon_to_stat1_a`() =
        runTest { testGbMicrotest("lcdon_to_stat1_a.gb") }

    @Test
    fun `lcdon_to_stat1_b`() =
        runTest { testGbMicrotest("lcdon_to_stat1_b.gb") }

    @Test
    fun `lcdon_to_stat1_c`() =
        runTest { testGbMicrotest("lcdon_to_stat1_c.gb") }

    @Test
    fun `lcdon_to_stat1_d`() =
        runTest { testGbMicrotest("lcdon_to_stat1_d.gb") }

    @Test
    fun `lcdon_to_stat1_e`() =
        runTest { testGbMicrotest("lcdon_to_stat1_e.gb") }

    @Test
    fun `lcdon_to_stat2_a`() =
        runTest { testGbMicrotest("lcdon_to_stat2_a.gb") }

    @Test
    fun `lcdon_to_stat2_b`() =
        runTest { testGbMicrotest("lcdon_to_stat2_b.gb") }

    @Test
    fun `lcdon_to_stat2_c`() =
        runTest { testGbMicrotest("lcdon_to_stat2_c.gb") }

    @Test
    fun `lcdon_to_stat2_d`() =
        runTest { testGbMicrotest("lcdon_to_stat2_d.gb") }

    @Test
    fun `lcdon_to_stat3_a`() =
        runTest { testGbMicrotest("lcdon_to_stat3_a.gb") }

    @Test
    fun `lcdon_to_stat3_b`() =
        runTest { testGbMicrotest("lcdon_to_stat3_b.gb") }

    @Test
    fun `lcdon_to_stat3_c`() =
        runTest { testGbMicrotest("lcdon_to_stat3_c.gb") }

    @Test
    fun `lcdon_to_stat3_d`() =
        runTest { testGbMicrotest("lcdon_to_stat3_d.gb") }

    @Test
    fun `lcdon_write_timing`() =
        runTest { testGbMicrotest("lcdon_write_timing.gb") }

    @Test
    fun `line_65_ly`() = runTest { testGbMicrotest("line_65_ly.gb") }

    @Test
    fun `line_144_oam_int_a`() =
        runTest { testGbMicrotest("line_144_oam_int_a.gb") }

    @Test
    fun `line_144_oam_int_b`() =
        runTest { testGbMicrotest("line_144_oam_int_b.gb") }

    @Test
    fun `line_144_oam_int_c`() =
        runTest { testGbMicrotest("line_144_oam_int_c.gb") }

    @Test
    fun `line_144_oam_int_d`() =
        runTest { testGbMicrotest("line_144_oam_int_d.gb") }

    @Test
    fun `line_153_ly_a`() = runTest { testGbMicrotest("line_153_ly_a.gb") }

    @Test
    fun `line_153_ly_b`() = runTest { testGbMicrotest("line_153_ly_b.gb") }

    @Test
    fun `line_153_ly_c`() = runTest { testGbMicrotest("line_153_ly_c.gb") }

    @Test
    fun `line_153_ly_d`() = runTest { testGbMicrotest("line_153_ly_d.gb") }

    @Test
    fun `line_153_ly_e`() = runTest { testGbMicrotest("line_153_ly_e.gb") }

    @Test
    fun `line_153_ly_f`() = runTest { testGbMicrotest("line_153_ly_f.gb") }

    @Test
    fun `line_153_lyc0_int_inc_sled`() =
        runTest { testGbMicrotest("line_153_lyc0_int_inc_sled.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_a`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_a.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_b`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_b.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_c`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_c.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_d`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_d.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_e`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_e.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_f`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_f.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_g`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_g.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_h`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_h.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_i`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_i.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_j`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_j.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_k`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_k.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_l`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_l.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_m`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_m.gb") }

    @Test
    fun `line_153_lyc0_stat_timing_n`() =
        runTest { testGbMicrotest("line_153_lyc0_stat_timing_n.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_a`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_a.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_b`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_b.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_c`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_c.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_d`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_d.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_e`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_e.gb") }

    @Test
    fun `line_153_lyc153_stat_timing_f`() =
        runTest { testGbMicrotest("line_153_lyc153_stat_timing_f.gb") }

    @Test
    fun `line_153_lyc_a`() = runTest { testGbMicrotest("line_153_lyc_a.gb") }

    @Test
    fun `line_153_lyc_b`() = runTest { testGbMicrotest("line_153_lyc_b.gb") }

    @Test
    fun `line_153_lyc_c`() = runTest { testGbMicrotest("line_153_lyc_c.gb") }

    @Test
    fun `line_153_lyc_int_a`() =
        runTest { testGbMicrotest("line_153_lyc_int_a.gb") }

    @Test
    fun `line_153_lyc_int_b`() =
        runTest { testGbMicrotest("line_153_lyc_int_b.gb") }

    @Test
    fun `ly_while_lcd_off`() =
        runTest { testGbMicrotest("ly_while_lcd_off.gb") }

    @Test
    fun `lyc1_int_halt_a`() = runTest { testGbMicrotest("lyc1_int_halt_a.gb") }

    @Test
    fun `lyc1_int_halt_b`() = runTest { testGbMicrotest("lyc1_int_halt_b.gb") }

    @Test
    fun `lyc1_int_if_edge_a`() =
        runTest { testGbMicrotest("lyc1_int_if_edge_a.gb") }

    @Test
    fun `lyc1_int_if_edge_b`() =
        runTest { testGbMicrotest("lyc1_int_if_edge_b.gb") }

    @Test
    fun `lyc1_int_if_edge_c`() =
        runTest { testGbMicrotest("lyc1_int_if_edge_c.gb") }

    @Test
    fun `lyc1_int_if_edge_d`() =
        runTest { testGbMicrotest("lyc1_int_if_edge_d.gb") }

    @Test
    fun `lyc1_int_nops_a`() = runTest { testGbMicrotest("lyc1_int_nops_a.gb") }

    @Test
    fun `lyc1_int_nops_b`() = runTest { testGbMicrotest("lyc1_int_nops_b.gb") }

    @Test
    fun `lyc1_write_timing_a`() =
        runTest { testGbMicrotest("lyc1_write_timing_a.gb") }

    @Test
    fun `lyc1_write_timing_b`() =
        runTest { testGbMicrotest("lyc1_write_timing_b.gb") }

    @Test
    fun `lyc1_write_timing_c`() =
        runTest { testGbMicrotest("lyc1_write_timing_c.gb") }

    @Test
    fun `lyc1_write_timing_d`() =
        runTest { testGbMicrotest("lyc1_write_timing_d.gb") }

    @Test
    fun `lyc2_int_halt_a`() = runTest { testGbMicrotest("lyc2_int_halt_a.gb") }

    @Test
    fun `lyc2_int_halt_b`() = runTest { testGbMicrotest("lyc2_int_halt_b.gb") }

    @Test
    fun `lyc_int_halt_a`() = runTest { testGbMicrotest("lyc_int_halt_a.gb") }

    @Test
    fun `lyc_int_halt_b`() = runTest { testGbMicrotest("lyc_int_halt_b.gb") }

    @Test
    fun `mbc1_ram_banks`() = runTest { testGbMicrotest("mbc1_ram_banks.gb") }

    @Test
    fun `mbc1_rom_banks`() = runTest { testGbMicrotest("mbc1_rom_banks.gb") }

    @Test
    fun `minimal`() = runTest { testGbMicrotest("minimal.gb") }

    @Test
    fun `mode2_stat_int_to_oam_unlock`() =
        runTest { testGbMicrotest("mode2_stat_int_to_oam_unlock.gb") }

    @Test
    fun `oam_int_halt_a`() = runTest { testGbMicrotest("oam_int_halt_a.gb") }

    @Test
    fun `oam_int_halt_b`() = runTest { testGbMicrotest("oam_int_halt_b.gb") }

    @Test
    fun `oam_int_if_edge_a`() =
        runTest { testGbMicrotest("oam_int_if_edge_a.gb") }

    @Test
    fun `oam_int_if_edge_b`() =
        runTest { testGbMicrotest("oam_int_if_edge_b.gb") }

    @Test
    fun `oam_int_if_edge_c`() =
        runTest { testGbMicrotest("oam_int_if_edge_c.gb") }

    @Test
    fun `oam_int_if_edge_d`() =
        runTest { testGbMicrotest("oam_int_if_edge_d.gb") }

    @Test
    fun `oam_int_if_level_c`() =
        runTest { testGbMicrotest("oam_int_if_level_c.gb") }

    @Test
    fun `oam_int_if_level_d`() =
        runTest { testGbMicrotest("oam_int_if_level_d.gb") }

    @Test
    fun `oam_int_inc_sled`() =
        runTest { testGbMicrotest("oam_int_inc_sled.gb") }

    @Test
    fun `oam_int_nops_a`() = runTest { testGbMicrotest("oam_int_nops_a.gb") }

    @Test
    fun `oam_int_nops_b`() = runTest { testGbMicrotest("oam_int_nops_b.gb") }

    @Test
    fun `oam_read_l0_a`() = runTest { testGbMicrotest("oam_read_l0_a.gb") }

    @Test
    fun `oam_read_l0_b`() = runTest { testGbMicrotest("oam_read_l0_b.gb") }

    @Test
    fun `oam_read_l0_c`() = runTest { testGbMicrotest("oam_read_l0_c.gb") }

    @Test
    fun `oam_read_l0_d`() = runTest { testGbMicrotest("oam_read_l0_d.gb") }

    @Test
    fun `oam_read_l1_a`() = runTest { testGbMicrotest("oam_read_l1_a.gb") }

    @Test
    fun `oam_read_l1_b`() = runTest { testGbMicrotest("oam_read_l1_b.gb") }

    @Test
    fun `oam_read_l1_c`() = runTest { testGbMicrotest("oam_read_l1_c.gb") }

    @Test
    fun `oam_read_l1_d`() = runTest { testGbMicrotest("oam_read_l1_d.gb") }

    @Test
    fun `oam_read_l1_e`() = runTest { testGbMicrotest("oam_read_l1_e.gb") }

    @Test
    fun `oam_read_l1_f`() = runTest { testGbMicrotest("oam_read_l1_f.gb") }

    @Test
    fun `oam_sprite_trashing`() =
        runTest { testGbMicrotest("oam_sprite_trashing.gb") }

    @Test
    fun `oam_write_l0_a`() = runTest { testGbMicrotest("oam_write_l0_a.gb") }

    @Test
    fun `oam_write_l0_b`() = runTest { testGbMicrotest("oam_write_l0_b.gb") }

    @Test
    fun `oam_write_l0_c`() = runTest { testGbMicrotest("oam_write_l0_c.gb") }

    @Test
    fun `oam_write_l0_d`() = runTest { testGbMicrotest("oam_write_l0_d.gb") }

    @Test
    fun `oam_write_l0_e`() = runTest { testGbMicrotest("oam_write_l0_e.gb") }

    @Test
    fun `oam_write_l1_a`() = runTest { testGbMicrotest("oam_write_l1_a.gb") }

    @Test
    fun `oam_write_l1_b`() = runTest { testGbMicrotest("oam_write_l1_b.gb") }

    @Test
    fun `oam_write_l1_c`() = runTest { testGbMicrotest("oam_write_l1_c.gb") }

    @Test
    fun `oam_write_l1_d`() = runTest { testGbMicrotest("oam_write_l1_d.gb") }

    @Test
    fun `oam_write_l1_e`() = runTest { testGbMicrotest("oam_write_l1_e.gb") }

    @Test
    fun `oam_write_l1_f`() = runTest { testGbMicrotest("oam_write_l1_f.gb") }

    @Test
    fun `poweron`() = runTest { testGbMicrotest("poweron.gb") }

    @Test
    fun `poweron_bgp_000`() = runTest { testGbMicrotest("poweron_bgp_000.gb") }

    @Test
    fun `poweron_div_000`() = runTest { testGbMicrotest("poweron_div_000.gb") }

    @Test
    fun `poweron_div_004`() = runTest { testGbMicrotest("poweron_div_004.gb") }

    @Test
    fun `poweron_div_005`() = runTest { testGbMicrotest("poweron_div_005.gb") }

    @Test
    fun `poweron_dma_000`() = runTest { testGbMicrotest("poweron_dma_000.gb") }

    @Test
    fun `poweron_if_000`() = runTest { testGbMicrotest("poweron_if_000.gb") }

    @Test
    fun `poweron_joy_000`() = runTest { testGbMicrotest("poweron_joy_000.gb") }

    @Test
    fun `poweron_lcdc_000`() =
        runTest { testGbMicrotest("poweron_lcdc_000.gb") }

    @Test
    fun `poweron_ly_000`() = runTest { testGbMicrotest("poweron_ly_000.gb") }

    @Test
    fun `poweron_ly_119`() = runTest { testGbMicrotest("poweron_ly_119.gb") }

    @Test
    fun `poweron_ly_120`() = runTest { testGbMicrotest("poweron_ly_120.gb") }

    @Test
    fun `poweron_ly_233`() = runTest { testGbMicrotest("poweron_ly_233.gb") }

    @Test
    fun `poweron_ly_234`() = runTest { testGbMicrotest("poweron_ly_234.gb") }

    @Test
    fun `poweron_lyc_000`() = runTest { testGbMicrotest("poweron_lyc_000.gb") }

    @Test
    fun `poweron_oam_000`() = runTest { testGbMicrotest("poweron_oam_000.gb") }

    @Test
    fun `poweron_oam_005`() = runTest { testGbMicrotest("poweron_oam_005.gb") }

    @Test
    fun `poweron_oam_006`() = runTest { testGbMicrotest("poweron_oam_006.gb") }

    @Test
    fun `poweron_oam_069`() = runTest { testGbMicrotest("poweron_oam_069.gb") }

    @Test
    fun `poweron_oam_070`() = runTest { testGbMicrotest("poweron_oam_070.gb") }

    @Test
    fun `poweron_oam_119`() = runTest { testGbMicrotest("poweron_oam_119.gb") }

    @Test
    fun `poweron_oam_120`() = runTest { testGbMicrotest("poweron_oam_120.gb") }

    @Test
    fun `poweron_oam_121`() = runTest { testGbMicrotest("poweron_oam_121.gb") }

    @Test
    fun `poweron_oam_183`() = runTest { testGbMicrotest("poweron_oam_183.gb") }

    @Test
    fun `poweron_oam_184`() = runTest { testGbMicrotest("poweron_oam_184.gb") }

    @Test
    fun `poweron_oam_233`() = runTest { testGbMicrotest("poweron_oam_233.gb") }

    @Test
    fun `poweron_oam_234`() = runTest { testGbMicrotest("poweron_oam_234.gb") }

    @Test
    fun `poweron_oam_235`() = runTest { testGbMicrotest("poweron_oam_235.gb") }

    @Test
    fun `poweron_obp0_000`() =
        runTest { testGbMicrotest("poweron_obp0_000.gb") }

    @Test
    fun `poweron_obp1_000`() =
        runTest { testGbMicrotest("poweron_obp1_000.gb") }

    @Test
    fun `poweron_sb_000`() = runTest { testGbMicrotest("poweron_sb_000.gb") }

    @Test
    fun `poweron_sc_000`() = runTest { testGbMicrotest("poweron_sc_000.gb") }

    @Test
    fun `poweron_scx_000`() = runTest { testGbMicrotest("poweron_scx_000.gb") }

    @Test
    fun `poweron_scy_000`() = runTest { testGbMicrotest("poweron_scy_000.gb") }

    @Test
    fun `poweron_stat_000`() =
        runTest { testGbMicrotest("poweron_stat_000.gb") }

    @Test
    fun `poweron_stat_005`() =
        runTest { testGbMicrotest("poweron_stat_005.gb") }

    @Test
    fun `poweron_stat_006`() =
        runTest { testGbMicrotest("poweron_stat_006.gb") }

    @Test
    fun `poweron_stat_007`() =
        runTest { testGbMicrotest("poweron_stat_007.gb") }

    @Test
    fun `poweron_stat_026`() =
        runTest { testGbMicrotest("poweron_stat_026.gb") }

    @Test
    fun `poweron_stat_027`() =
        runTest { testGbMicrotest("poweron_stat_027.gb") }

    @Test
    fun `poweron_stat_069`() =
        runTest { testGbMicrotest("poweron_stat_069.gb") }

    @Test
    fun `poweron_stat_070`() =
        runTest { testGbMicrotest("poweron_stat_070.gb") }

    @Test
    fun `poweron_stat_119`() =
        runTest { testGbMicrotest("poweron_stat_119.gb") }

    @Test
    fun `poweron_stat_120`() =
        runTest { testGbMicrotest("poweron_stat_120.gb") }

    @Test
    fun `poweron_stat_121`() =
        runTest { testGbMicrotest("poweron_stat_121.gb") }

    @Test
    fun `poweron_stat_140`() =
        runTest { testGbMicrotest("poweron_stat_140.gb") }

    @Test
    fun `poweron_stat_141`() =
        runTest { testGbMicrotest("poweron_stat_141.gb") }

    @Test
    fun `poweron_stat_183`() =
        runTest { testGbMicrotest("poweron_stat_183.gb") }

    @Test
    fun `poweron_stat_184`() =
        runTest { testGbMicrotest("poweron_stat_184.gb") }

    @Test
    fun `poweron_stat_234`() =
        runTest { testGbMicrotest("poweron_stat_234.gb") }

    @Test
    fun `poweron_stat_235`() =
        runTest { testGbMicrotest("poweron_stat_235.gb") }

    @Test
    fun `poweron_tac_000`() = runTest { testGbMicrotest("poweron_tac_000.gb") }

    @Test
    fun `poweron_tima_000`() =
        runTest { testGbMicrotest("poweron_tima_000.gb") }

    @Test
    fun `poweron_tma_000`() = runTest { testGbMicrotest("poweron_tma_000.gb") }

    @Test
    fun `poweron_vram_000`() =
        runTest { testGbMicrotest("poweron_vram_000.gb") }

    @Test
    fun `poweron_vram_025`() =
        runTest { testGbMicrotest("poweron_vram_025.gb") }

    @Test
    fun `poweron_vram_026`() =
        runTest { testGbMicrotest("poweron_vram_026.gb") }

    @Test
    fun `poweron_vram_069`() =
        runTest { testGbMicrotest("poweron_vram_069.gb") }

    @Test
    fun `poweron_vram_070`() =
        runTest { testGbMicrotest("poweron_vram_070.gb") }

    @Test
    fun `poweron_vram_139`() =
        runTest { testGbMicrotest("poweron_vram_139.gb") }

    @Test
    fun `poweron_vram_140`() =
        runTest { testGbMicrotest("poweron_vram_140.gb") }

    @Test
    fun `poweron_vram_183`() =
        runTest { testGbMicrotest("poweron_vram_183.gb") }

    @Test
    fun `poweron_vram_184`() =
        runTest { testGbMicrotest("poweron_vram_184.gb") }

    @Test
    fun `poweron_wx_000`() = runTest { testGbMicrotest("poweron_wx_000.gb") }

    @Test
    fun `poweron_wy_000`() = runTest { testGbMicrotest("poweron_wy_000.gb") }

    @Test
    fun `ppu_scx_vs_bgp`() = runTest { testGbMicrotest("ppu_scx_vs_bgp.gb") }

    @Test
    fun `ppu_sprite0_scx0_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx0_a.gb") }

    @Test
    fun `ppu_sprite0_scx0_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx0_b.gb") }

    @Test
    fun `ppu_sprite0_scx1_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx1_a.gb") }

    @Test
    fun `ppu_sprite0_scx1_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx1_b.gb") }

    @Test
    fun `ppu_sprite0_scx2_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx2_a.gb") }

    @Test
    fun `ppu_sprite0_scx2_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx2_b.gb") }

    @Test
    fun `ppu_sprite0_scx3_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx3_a.gb") }

    @Test
    fun `ppu_sprite0_scx3_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx3_b.gb") }

    @Test
    fun `ppu_sprite0_scx4_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx4_a.gb") }

    @Test
    fun `ppu_sprite0_scx4_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx4_b.gb") }

    @Test
    fun `ppu_sprite0_scx5_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx5_a.gb") }

    @Test
    fun `ppu_sprite0_scx5_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx5_b.gb") }

    @Test
    fun `ppu_sprite0_scx6_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx6_a.gb") }

    @Test
    fun `ppu_sprite0_scx6_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx6_b.gb") }

    @Test
    fun `ppu_sprite0_scx7_a`() =
        runTest { testGbMicrotest("ppu_sprite0_scx7_a.gb") }

    @Test
    fun `ppu_sprite0_scx7_b`() =
        runTest { testGbMicrotest("ppu_sprite0_scx7_b.gb") }

    @Test
    fun `ppu_sprite_testbench`() =
        runTest { testGbMicrotest("ppu_sprite_testbench.gb") }

    @Test
    fun `ppu_spritex_vs_scx`() =
        runTest { testGbMicrotest("ppu_spritex_vs_scx.gb") }

    @Test
    fun `ppu_win_vs_wx`() = runTest { testGbMicrotest("ppu_win_vs_wx.gb") }

    @Test
    fun `ppu_wx_early`() = runTest { testGbMicrotest("ppu_wx_early.gb") }

    @Test
    fun `sprite4_0_a`() = runTest { testGbMicrotest("sprite4_0_a.gb") }

    @Test
    fun `sprite4_0_b`() = runTest { testGbMicrotest("sprite4_0_b.gb") }

    @Test
    fun `sprite4_1_a`() = runTest { testGbMicrotest("sprite4_1_a.gb") }

    @Test
    fun `sprite4_1_b`() = runTest { testGbMicrotest("sprite4_1_b.gb") }

    @Test
    fun `sprite4_2_a`() = runTest { testGbMicrotest("sprite4_2_a.gb") }

    @Test
    fun `sprite4_2_b`() = runTest { testGbMicrotest("sprite4_2_b.gb") }

    @Test
    fun `sprite4_3_a`() = runTest { testGbMicrotest("sprite4_3_a.gb") }

    @Test
    fun `sprite4_3_b`() = runTest { testGbMicrotest("sprite4_3_b.gb") }

    @Test
    fun `sprite4_4_a`() = runTest { testGbMicrotest("sprite4_4_a.gb") }

    @Test
    fun `sprite4_4_b`() = runTest { testGbMicrotest("sprite4_4_b.gb") }

    @Test
    fun `sprite4_5_a`() = runTest { testGbMicrotest("sprite4_5_a.gb") }

    @Test
    fun `sprite4_5_b`() = runTest { testGbMicrotest("sprite4_5_b.gb") }

    @Test
    fun `sprite4_6_a`() = runTest { testGbMicrotest("sprite4_6_a.gb") }

    @Test
    fun `sprite4_6_b`() = runTest { testGbMicrotest("sprite4_6_b.gb") }

    @Test
    fun `sprite4_7_a`() = runTest { testGbMicrotest("sprite4_7_a.gb") }

    @Test
    fun `sprite4_7_b`() = runTest { testGbMicrotest("sprite4_7_b.gb") }

    @Test
    fun `sprite_0_a`() = runTest { testGbMicrotest("sprite_0_a.gb") }

    @Test
    fun `sprite_0_b`() = runTest { testGbMicrotest("sprite_0_b.gb") }

    @Test
    fun `sprite_1_a`() = runTest { testGbMicrotest("sprite_1_a.gb") }

    @Test
    fun `sprite_1_b`() = runTest { testGbMicrotest("sprite_1_b.gb") }

    @Test
    fun `stat_write_glitch_l0_a`() =
        runTest { testGbMicrotest("stat_write_glitch_l0_a.gb") }

    @Test
    fun `stat_write_glitch_l0_b`() =
        runTest { testGbMicrotest("stat_write_glitch_l0_b.gb") }

    @Test
    fun `stat_write_glitch_l0_c`() =
        runTest { testGbMicrotest("stat_write_glitch_l0_c.gb") }

    @Test
    fun `stat_write_glitch_l1_a`() =
        runTest { testGbMicrotest("stat_write_glitch_l1_a.gb") }

    @Test
    fun `stat_write_glitch_l1_b`() =
        runTest { testGbMicrotest("stat_write_glitch_l1_b.gb") }

    @Test
    fun `stat_write_glitch_l1_c`() =
        runTest { testGbMicrotest("stat_write_glitch_l1_c.gb") }

    @Test
    fun `stat_write_glitch_l1_d`() =
        runTest { testGbMicrotest("stat_write_glitch_l1_d.gb") }

    @Test
    fun `stat_write_glitch_l143_a`() =
        runTest { testGbMicrotest("stat_write_glitch_l143_a.gb") }

    @Test
    fun `stat_write_glitch_l143_b`() =
        runTest { testGbMicrotest("stat_write_glitch_l143_b.gb") }

    @Test
    fun `stat_write_glitch_l143_c`() =
        runTest { testGbMicrotest("stat_write_glitch_l143_c.gb") }

    @Test
    fun `stat_write_glitch_l143_d`() =
        runTest { testGbMicrotest("stat_write_glitch_l143_d.gb") }

    @Test
    fun `stat_write_glitch_l154_a`() =
        runTest { testGbMicrotest("stat_write_glitch_l154_a.gb") }

    @Test
    fun `stat_write_glitch_l154_b`() =
        runTest { testGbMicrotest("stat_write_glitch_l154_b.gb") }

    @Test
    fun `stat_write_glitch_l154_c`() =
        runTest { testGbMicrotest("stat_write_glitch_l154_c.gb") }

    @Test
    fun `stat_write_glitch_l154_d`() =
        runTest { testGbMicrotest("stat_write_glitch_l154_d.gb") }

    @Test
    fun `toggle_lcdc`() = runTest { testGbMicrotest("toggle_lcdc.gb") }

    @Test
    fun `vblank2_int_halt_a`() =
        runTest { testGbMicrotest("vblank2_int_halt_a.gb") }

    @Test
    fun `vblank2_int_halt_b`() =
        runTest { testGbMicrotest("vblank2_int_halt_b.gb") }

    @Test
    fun `vblank2_int_if_a`() =
        runTest { testGbMicrotest("vblank2_int_if_a.gb") }

    @Test
    fun `vblank2_int_if_b`() =
        runTest { testGbMicrotest("vblank2_int_if_b.gb") }

    @Test
    fun `vblank2_int_if_c`() =
        runTest { testGbMicrotest("vblank2_int_if_c.gb") }

    @Test
    fun `vblank2_int_if_d`() =
        runTest { testGbMicrotest("vblank2_int_if_d.gb") }

    @Test
    fun `vblank2_int_inc_sled`() =
        runTest { testGbMicrotest("vblank2_int_inc_sled.gb") }

    @Test
    fun `vblank2_int_nops_a`() =
        runTest { testGbMicrotest("vblank2_int_nops_a.gb") }

    @Test
    fun `vblank2_int_nops_b`() =
        runTest { testGbMicrotest("vblank2_int_nops_b.gb") }

    @Test
    fun `vblank_int_halt_a`() =
        runTest { testGbMicrotest("vblank_int_halt_a.gb") }

    @Test
    fun `vblank_int_halt_b`() =
        runTest { testGbMicrotest("vblank_int_halt_b.gb") }

    @Test
    fun `vblank_int_if_a`() = runTest { testGbMicrotest("vblank_int_if_a.gb") }

    @Test
    fun `vblank_int_if_b`() = runTest { testGbMicrotest("vblank_int_if_b.gb") }

    @Test
    fun `vblank_int_if_c`() = runTest { testGbMicrotest("vblank_int_if_c.gb") }

    @Test
    fun `vblank_int_if_d`() = runTest { testGbMicrotest("vblank_int_if_d.gb") }

    @Test
    fun `vblank_int_inc_sled`() =
        runTest { testGbMicrotest("vblank_int_inc_sled.gb") }

    @Test
    fun `vblank_int_nops_a`() =
        runTest { testGbMicrotest("vblank_int_nops_a.gb") }

    @Test
    fun `vblank_int_nops_b`() =
        runTest { testGbMicrotest("vblank_int_nops_b.gb") }

    @Test
    fun `vram_read_l0_a`() = runTest { testGbMicrotest("vram_read_l0_a.gb") }

    @Test
    fun `vram_read_l0_b`() = runTest { testGbMicrotest("vram_read_l0_b.gb") }

    @Test
    fun `vram_read_l0_c`() = runTest { testGbMicrotest("vram_read_l0_c.gb") }

    @Test
    fun `vram_read_l0_d`() = runTest { testGbMicrotest("vram_read_l0_d.gb") }

    @Test
    fun `vram_read_l1_a`() = runTest { testGbMicrotest("vram_read_l1_a.gb") }

    @Test
    fun `vram_read_l1_b`() = runTest { testGbMicrotest("vram_read_l1_b.gb") }

    @Test
    fun `vram_read_l1_c`() = runTest { testGbMicrotest("vram_read_l1_c.gb") }

    @Test
    fun `vram_read_l1_d`() = runTest { testGbMicrotest("vram_read_l1_d.gb") }

    @Test
    fun `vram_write_l0_a`() = runTest { testGbMicrotest("vram_write_l0_a.gb") }

    @Test
    fun `vram_write_l0_b`() = runTest { testGbMicrotest("vram_write_l0_b.gb") }

    @Test
    fun `vram_write_l0_c`() = runTest { testGbMicrotest("vram_write_l0_c.gb") }

    @Test
    fun `vram_write_l0_d`() = runTest { testGbMicrotest("vram_write_l0_d.gb") }

    @Test
    fun `vram_write_l1_a`() = runTest { testGbMicrotest("vram_write_l1_a.gb") }

    @Test
    fun `vram_write_l1_b`() = runTest { testGbMicrotest("vram_write_l1_b.gb") }

    @Test
    fun `vram_write_l1_c`() = runTest { testGbMicrotest("vram_write_l1_c.gb") }

    @Test
    fun `vram_write_l1_d`() = runTest { testGbMicrotest("vram_write_l1_d.gb") }

    @Test
    fun `wave_write_to_0xC003`() =
        runTest { testGbMicrotest("wave_write_to_0xC003.gb") }

    @Test
    fun `win0_a`() = runTest { testGbMicrotest("win0_a.gb") }

    @Test
    fun `win0_b`() = runTest { testGbMicrotest("win0_b.gb") }

    @Test
    fun `win0_scx3_a`() = runTest { testGbMicrotest("win0_scx3_a.gb") }

    @Test
    fun `win0_scx3_b`() = runTest { testGbMicrotest("win0_scx3_b.gb") }

    @Test
    fun `win1_a`() = runTest { testGbMicrotest("win1_a.gb") }

    @Test
    fun `win1_b`() = runTest { testGbMicrotest("win1_b.gb") }

    @Test
    fun `win2_a`() = runTest { testGbMicrotest("win2_a.gb") }

    @Test
    fun `win2_b`() = runTest { testGbMicrotest("win2_b.gb") }

    @Test
    fun `win3_a`() = runTest { testGbMicrotest("win3_a.gb") }

    @Test
    fun `win3_b`() = runTest { testGbMicrotest("win3_b.gb") }

    @Test
    fun `win4_a`() = runTest { testGbMicrotest("win4_a.gb") }

    @Test
    fun `win4_b`() = runTest { testGbMicrotest("win4_b.gb") }

    @Test
    fun `win5_a`() = runTest { testGbMicrotest("win5_a.gb") }

    @Test
    fun `win5_b`() = runTest { testGbMicrotest("win5_b.gb") }

    @Test
    fun `win6_a`() = runTest { testGbMicrotest("win6_a.gb") }

    @Test
    fun `win6_b`() = runTest { testGbMicrotest("win6_b.gb") }

    @Test
    fun `win7_a`() = runTest { testGbMicrotest("win7_a.gb") }

    @Test
    fun `win7_b`() = runTest { testGbMicrotest("win7_b.gb") }

    @Test
    fun `win8_a`() = runTest { testGbMicrotest("win8_a.gb") }

    @Test
    fun `win8_b`() = runTest { testGbMicrotest("win8_b.gb") }

    @Test
    fun `win9_a`() = runTest { testGbMicrotest("win9_a.gb") }

    @Test
    fun `win9_b`() = runTest { testGbMicrotest("win9_b.gb") }

    @Test
    fun `win10_a`() = runTest { testGbMicrotest("win10_a.gb") }

    @Test
    fun `win10_b`() = runTest { testGbMicrotest("win10_b.gb") }

    @Test
    fun `win10_scx3_a`() = runTest { testGbMicrotest("win10_scx3_a.gb") }

    @Test
    fun `win10_scx3_b`() = runTest { testGbMicrotest("win10_scx3_b.gb") }

    @Test
    fun `win11_a`() = runTest { testGbMicrotest("win11_a.gb") }

    @Test
    fun `win11_b`() = runTest { testGbMicrotest("win11_b.gb") }

    @Test
    fun `win12_a`() = runTest { testGbMicrotest("win12_a.gb") }

    @Test
    fun `win12_b`() = runTest { testGbMicrotest("win12_b.gb") }

    @Test
    fun `win13_a`() = runTest { testGbMicrotest("win13_a.gb") }

    @Test
    fun `win13_b`() = runTest { testGbMicrotest("win13_b.gb") }

    @Test
    fun `win14_a`() = runTest { testGbMicrotest("win14_a.gb") }

    @Test
    fun `win14_b`() = runTest { testGbMicrotest("win14_b.gb") }

    @Test
    fun `win15_a`() = runTest { testGbMicrotest("win15_a.gb") }

    @Test
    fun `win15_b`() = runTest { testGbMicrotest("win15_b.gb") }

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