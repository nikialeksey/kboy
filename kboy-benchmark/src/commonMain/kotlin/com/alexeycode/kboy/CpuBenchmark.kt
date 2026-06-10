package com.alexeycode.kboy

import com.alexeycode.kboy.gb.Gb
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
import com.goncalossilva.resources.Resource
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
open class CpuBenchmark {

    private val serial = BufferSerial()
    private lateinit var gb: Gb

    @Setup
    fun setup() {
        val bytes: ByteArray = Resource("files/blargg/01-special.gb").readBytes()
        val cartridge = GbCartridge(GbCartridgeData(bytes))

        val memory = cartridge.memory()
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val dma = GbDma()
        val joypad = GbJoypad(interrupts)
        val lcdStatus = GbLcdStatus()
        val lcdControl = GbLcdControl()
        val palette = GbPalette()
        val background = GbBackground()
        val window = GbWindow()

        val cpu = GbCpu(
            r = GbRegisters(),
            mem = GbBus(
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
            ),
            interrupts = interrupts
        )
        gb = SimpleGb(
            timer = timer,
            cpu = cpu,
            dma = GbDmaTransfer(memory, dma),
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
    }

    @Benchmark
    fun benchmark() {
        while (true) {
            gb.run(100_000)
            val outputMessage = serial.asByteArray().decodeToString()
            if (outputMessage.contains("Passed") || outputMessage.contains("Failed")) {
                break
            }
        }
    }

}