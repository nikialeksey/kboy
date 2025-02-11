package com.alexeycode.kboy.gb.mem

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.timer.Timer
import com.alexeycode.kboy.gb.joypad.Joypad
import com.alexeycode.kboy.gb.ppu.Background
import com.alexeycode.kboy.gb.ppu.LcdControl
import com.alexeycode.kboy.gb.ppu.LcdStatus
import com.alexeycode.kboy.gb.ppu.Window
import com.alexeycode.kboy.gb.serial.Serial

class GbMemory : Memory {

    private val data: Array<Int>
    private val timer: Timer
    private val interrupts: Interrupts
    private val serial: Serial
    private val joypad: Joypad
    private val lcdStatus: LcdStatus
    private val lcdControl: LcdControl
    private val background: Background
    private val window: Window

    constructor(
        interrupts: Interrupts,
        timer: Timer,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        background: Background,
        window: Window
    ) : this(
        0xFFFF + 1,
        interrupts,
        timer,
        serial,
        joypad,
        lcdStatus,
        lcdControl,
        background,
        window
    )

    constructor(
        size: Int,
        interrupts: Interrupts,
        timer: Timer,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        background: Background,
        window: Window
    ) : this(
        Array(size) { 0 },
        interrupts,
        timer,
        serial,
        joypad,
        lcdStatus,
        lcdControl,
        background,
        window
    )

    constructor(
        data: Array<Int>,
        interrupts: Interrupts,
        timer: Timer,
        serial: Serial,
        joypad: Joypad,
        lcdStatus: LcdStatus,
        lcdControl: LcdControl,
        background: Background,
        window: Window
    ) {
        this.data = data
        this.timer = timer
        this.interrupts = interrupts
        this.serial = serial
        this.joypad = joypad
        this.lcdStatus = lcdStatus
        this.lcdControl = lcdControl
        this.background = background
        this.window = window
    }

    override fun read8(address: Int): Int {
        return if (address == 0xFF00) {
            joypad.get()
        } else if (address == 0xFF04) {
            timer.div()
        } else if (address == 0xFF05) {
            timer.tima()
        } else if (address == 0xFF06) {
            timer.tma()
        } else if (address == 0xFF07) {
            timer.tac()
        } else if (address == 0xFF0F) {
            interrupts.ifFlag()
        } else if (address == 0xFF40) {
            lcdControl.get()
        } else if (address == 0xFF41) {
            lcdStatus.stat()
        } else if (address == 0xFF42) {
            background.scy()
        } else if (address == 0xFF43) {
            background.scx()
        } else if (address == 0xFF44) {
            lcdStatus.ly()
        } else if (address == 0xFF45) {
            lcdStatus.lyc()
        } else if (address == 0xFF4A) {
            window.wy()
        } else if (address == 0xFF4B) {
            window.wx()
        } else if (address == 0xFFFF) {
            interrupts.ieFlag()
        } else {
            data[address]
        }
    }

    override fun write8(address: Int, value: Int) {
        if (address == 0xFF00) {
            joypad.update(value)
        } else if (address == 0xFF01) {
            serial.put(value)
        } else if (address == 0xFF04) {
            timer.resetDiv()
        } else if (address == 0xFF05) {
            timer.updateTima(value)
        } else if (address == 0xFF06) {
            timer.updateTma(value)
        } else if (address == 0xFF07) {
            timer.updateTac(value)
        } else if (address == 0xFF0F) {
            interrupts.updateIfFlag(value)
        } else if (address == 0xFF40) {
            lcdControl.update(value)
        } else if (address == 0xFF41) {
            lcdStatus.updateStat(value)
        } else if (address == 0xFF42) {
            background.updateScy(value)
        } else if (address == 0xFF43) {
            background.updateScx(value)
        } else if (address == 0xFF44) {
            // LY read only
        } else if (address == 0xFF45) {
            lcdStatus.updateLyc(value)
        } else if (address == 0xFF4A) {
            window.updateWy(value)
        } else if (address == 0xFF4B) {
            window.updateWx(value)
        } else if (address == 0xFFFF) {
            interrupts.updateIeFlag(value)
        } else {
            data[address] = value
        }
    }
}