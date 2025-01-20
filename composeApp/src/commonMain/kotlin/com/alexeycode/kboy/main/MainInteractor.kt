package com.alexeycode.kboy.main

import androidx.compose.ui.graphics.ImageBitmap
import com.alexeycode.kboy.gb.Gb
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
import com.alexeycode.kboy.gb.ppu.GbPpu
import com.alexeycode.kboy.gb.serial.BufferSerial
import com.alexeycode.kboy.io.readFile
import kboy.composeapp.generated.resources.Res
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.ExperimentalResourceApi

class MainInteractor {

    private var gb: Gb? = null

    @OptIn(ExperimentalResourceApi::class)
    suspend fun prepareGb(romUri: String): Flow<ImageBitmap> {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val serial = BufferSerial()

        val memory = GbMemory(interrupts, timer, serial)
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
        val ppu = GbPpu(memory)
        gb = SimpleGb(timer, cpu, ppu)

        return ppu.screen()
    }
}