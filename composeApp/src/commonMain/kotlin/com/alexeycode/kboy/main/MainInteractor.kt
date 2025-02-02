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
import com.alexeycode.kboy.gb.mem.GbMemory
import com.alexeycode.kboy.gb.ppu.GbLcdStatus
import com.alexeycode.kboy.gb.ppu.GbPpu
import com.alexeycode.kboy.gb.ppu.ImageBitmap
import com.alexeycode.kboy.gb.serial.BufferSerial
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
    suspend fun prepareGb(scope: CoroutineScope, romUri: String): Flow<ImageBitmap> {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val serial = BufferSerial()
        val lcdStatus = GbLcdStatus()

        val memory = GbMemory(interrupts, timer, serial, lcdStatus)
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
        val ppu = GbPpu(memory, lcdStatus)
        val gb = SimpleGb(timer, cpu, ppu)

        scope.launch {
            withContext(Dispatchers.Default) {
                while (isActive) {
                    gb.run(1)
                }
            }
        }

        return ppu.screen()
    }
}