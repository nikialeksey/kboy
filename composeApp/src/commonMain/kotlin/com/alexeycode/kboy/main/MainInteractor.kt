package com.alexeycode.kboy.main

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.GbCpu
import com.alexeycode.kboy.gb.cpu.instructions.GbCartridgeInstructions
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.opcodes.GbOpcodes
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
import com.alexeycode.kboy.gb.ppu.Screen
import com.alexeycode.kboy.gb.serial.BufferSerial
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.readFile
import kboy.composeapp.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

class MainInteractor {

    @OptIn(ExperimentalResourceApi::class)
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

        val memory = GbMemory(interrupts, timer, dma, serial, joypad, lcdStatus, lcdControl, palette, background, window)
        val dmaTransfer = GbDmaTransfer(memory, dma)
        val cartridge = GbCartridge(GbCartridgeData(readFile(romUri)))
        cartridge.upload(memory)

        val registers = InMemoryRegisters()
        val opcodes = GbOpcodes(Res.readBytes("files/Opcodes.json"))
        val instructions = GbCartridgeInstructions(opcodes, memory)
        val cpu = GbCpu(
            registers,
            memory,
            interrupts,
            instructions
        )
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
                while (isActive) {
                    gb.run(1)
                }
            }
        }

        return ppu.screen()
    }
}