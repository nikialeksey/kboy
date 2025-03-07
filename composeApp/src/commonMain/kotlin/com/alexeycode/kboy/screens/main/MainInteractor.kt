package com.alexeycode.kboy.screens.main

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.GbCpu
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
import com.alexeycode.kboy.gb.ppu.RENDER_CYCLES
import com.alexeycode.kboy.gb.ppu.Screen
import com.alexeycode.kboy.gb.serial.BufferSerial
import com.alexeycode.kboy.host.Time
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.FileSystem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainInteractor(
    private val fileSystem: FileSystem,
    private val time: Time,
) {

    suspend fun prepareGb(scope: CoroutineScope, romUri: String, controller: Controller): Flow<Screen> {
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

        val cartridge = GbCartridge(GbCartridgeData(fileSystem.readFile(romUri)))
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
        val cpu = GbCpu(registers, bus, interrupts)
        val ppu = GbPpu(interrupts, memory, lcdStatus, lcdControl, palette, background, window)
        val gb = SimpleGb(timer, cpu, dmaTransfer, ppu)

        scope.launch {
            launch { controller.a().collect { pressed -> if (pressed) joypad.a().press() else joypad.a().release() } }
            launch { controller.b().collect { pressed -> if (pressed) joypad.b().press() else joypad.b().release() } }
            launch { controller.select().collect { pressed -> if (pressed) joypad.select().press() else joypad.select().release() } }
            launch { controller.start().collect { pressed -> if (pressed) joypad.start().press() else joypad.start().release() } }
            launch { controller.right().collect { pressed -> if (pressed) joypad.right().press() else joypad.right().release() } }
            launch { controller.left().collect { pressed -> if (pressed) joypad.left().press() else joypad.left().release() } }
            launch { controller.up().collect { pressed -> if (pressed) joypad.up().press() else joypad.up().release() } }
            launch { controller.down().collect { pressed -> if (pressed) joypad.down().press() else joypad.down().release() } }
            withContext(Dispatchers.Default) {
                var clockCyclesSinceLastFrame = 0
                var timeSinceLastFrame = time.currentTimeMs()

                while (isActive) {
                    // 70_224 clock ticks per frame
                    // we want to have 60 fps
                    // and CPU tick is about 70_224 / 4 fps
                    // so if we will do 1000 CPU ticks we can be sure
                    // that this cycle block will be executed less than one frame
                    // TODO think about serial transfers
                    // TODO think about audio unit
                    val clockCycles = gb.run(1000)
                    clockCyclesSinceLastFrame += clockCycles
                    if (clockCyclesSinceLastFrame > RENDER_CYCLES) {
                        // then we need to wait sometime to sync timings
                        clockCyclesSinceLastFrame %= RENDER_CYCLES
                        // each frame duration 1/60 seconds, or 17ms
                        var lastTimeMs = 0L
                        while (time.currentTimeMs().apply { lastTimeMs = this@apply } - timeSinceLastFrame < 16) {
                            delay(1)
                        }
                        timeSinceLastFrame = lastTimeMs
                    }
                }
            }
        }

        return ppu.screen()
    }
}