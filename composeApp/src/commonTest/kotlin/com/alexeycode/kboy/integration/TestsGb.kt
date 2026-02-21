package com.alexeycode.kboy.integration

import com.alexeycode.kboy.gb.Gb
import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.Cartridge
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
import com.alexeycode.kboy.gb.serial.Serial

class TestsGb private constructor(
    private val origin: Gb
) : Gb {

    constructor(
        cartridge: Cartridge,
        serial: Serial
    ) : this(buildGb(cartridge, serial))

    override fun run(cpuCycles: Int): Int {
        return origin.run(cpuCycles)
    }
}

private fun buildGb(cartridge: Cartridge, serial: Serial): Gb {
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
    val gb = SimpleGb(
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
    return gb
}