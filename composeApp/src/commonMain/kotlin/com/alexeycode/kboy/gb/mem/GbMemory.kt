package com.alexeycode.kboy.gb.mem

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.timer.Timer
import com.alexeycode.kboy.gb.joypad.Joypad
import com.alexeycode.kboy.gb.ppu.Background
import com.alexeycode.kboy.gb.ppu.LcdControl
import com.alexeycode.kboy.gb.ppu.LcdStatus
import com.alexeycode.kboy.gb.ppu.Palette
import com.alexeycode.kboy.gb.ppu.Window
import com.alexeycode.kboy.gb.serial.Serial

class GbMemory : Memory {

    private val data: Array<Int>
    private val timer: Timer
    private val dma: Dma
    private val interrupts: Interrupts
    private val serial: Serial
    private val joypad: Joypad
    private val lcdStatus: LcdStatus
    private val lcdControl: LcdControl
    private val palette: Palette
    private val background: Background
    private val window: Window

    constructor(
        interrupts: Interrupts,
        timer: Timer,
        dma: Dma,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        palette: Palette,
        background: Background,
        window: Window
    ) : this(
        0xFFFF + 1,
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

    constructor(
        size: Int,
        interrupts: Interrupts,
        timer: Timer,
        dma: Dma,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        palette: Palette,
        background: Background,
        window: Window
    ) : this(
        Array(size) { 0 },
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

    constructor(
        data: Array<Int>,
        interrupts: Interrupts,
        timer: Timer,
        dma: Dma,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        palette: Palette,
        background: Background,
        window: Window
    ) {
        this.data = data
        this.timer = timer
        this.dma = dma
        this.interrupts = interrupts
        this.serial = serial
        this.joypad = joypad
        this.lcdStatus = lcdStatus
        this.lcdControl = lcdControl
        this.palette = palette
        this.background = background
        this.window = window
    }

    override fun read8(address: Int): Int {
        return if (address < 0xFF00) {
            data[address]
        } else {
            when (address) {
                0xFF00 -> joypad.get()
                0xFF04 -> timer.div()
                0xFF05 -> timer.tima()
                0xFF06 -> timer.tma()
                0xFF07 -> timer.tac()
                0xFF0F -> interrupts.ifFlag()
                0xFF40 -> lcdControl.get()
                0xFF41 -> lcdStatus.stat()
                0xFF42 -> background.scy()
                0xFF43 -> background.scx()
                0xFF44 -> lcdStatus.ly()
                0xFF45 -> lcdStatus.lyc()
                0xFF46 -> dma.value()
                0xFF47 -> palette.getBgp()
                0xFF48 -> palette.getObp0()
                0xFF49 -> palette.getObp1()
                0xFF4A -> window.wy()
                0xFF4B -> window.wx()
                0xFFFF -> interrupts.ieFlag()
                else -> data[address]
            }
        }
    }

    override fun write8(address: Int, value: Int) {
        if (address < 0xFF00) {
            data[address] = value
        } else {
            when (address) {
                0xFF00 -> joypad.update(value)
                0xFF01 -> serial.put(value)
                0xFF04 -> timer.resetDiv()
                0xFF05 -> timer.updateTima(value)
                0xFF06 -> timer.updateTma(value)
                0xFF07 -> timer.updateTac(value)
                0xFF0F -> interrupts.updateIfFlag(value)
                0xFF40 -> lcdControl.update(value)
                0xFF41 -> lcdStatus.updateStat(value)
                0xFF42 -> background.updateScy(value)
                0xFF43 -> background.updateScx(value)
                0xFF44 -> {
                    // LY read only
                }

                0xFF45 -> lcdStatus.updateLyc(value)
                0xFF46 -> dma.updateValue(value)
                0xFF47 -> palette.updateBgp(value)
                0xFF48 -> palette.updateObp0(value)
                0xFF49 -> palette.updateObp1(value)
                0xFF4A -> window.updateWy(value)
                0xFF4B -> window.updateWx(value)
                0xFFFF -> interrupts.updateIeFlag(value)
                else -> data[address] = value
            }
        }
    }
}