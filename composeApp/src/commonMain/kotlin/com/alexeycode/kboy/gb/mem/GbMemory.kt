package com.alexeycode.kboy.gb.mem

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.timer.Timer
import com.alexeycode.kboy.gb.serial.Serial

class GbMemory : Memory {

    private val data: Array<Int>
    private val timer: Timer
    private val interrupts: Interrupts
    private val serial: Serial

    constructor(interrupts: Interrupts, timer: Timer, serial: Serial) : this(
        0xFFFF + 1,
        interrupts,
        timer,
        serial
    )

    constructor(size: Int, interrupts: Interrupts, timer: Timer, serial: Serial) : this(
        Array(size) { 0 },
        interrupts,
        timer,
        serial
    )

    constructor(
        data: Array<Int>,
        interrupts: Interrupts,
        timer: Timer,
        serial: Serial
    ) {
        this.data = data
        this.timer = timer
        this.interrupts = interrupts
        this.serial = serial
    }

    override fun read8(address: Int): Int {
        /**
         * 0000-3FFF   16KB ROM Bank 00     (in cartridge, fixed at bank 00)
         * 4000-7FFF   16KB ROM Bank 01..NN (in cartridge, switchable bank number)
         * 8000-9FFF   8KB Video RAM (VRAM) (switchable bank 0-1 in CGB Mode)
         * A000-BFFF   8KB External RAM     (in cartridge, switchable bank, if any)
         * C000-CFFF   4KB Work RAM Bank 0 (WRAM)
         * D000-DFFF   4KB Work RAM Bank 1 (WRAM)  (switchable bank 1-7 in CGB Mode)
         * E000-FDFF   Same as C000-DDFF (ECHO)    (typically not used)
         * FE00-FE9F   Sprite Attribute Table (OAM)
         * FEA0-FEFF   Not Usable
         * FF00-FF7F   I/O Ports
         * FF80-FFFE   High RAM (HRAM)
         * FFFF        Interrupt Enable Register
         * */
        return if (address == 0xFF04) {
            timer.div()
        } else if (address == 0xFF05) {
            timer.tima()
        } else if (address == 0xFF06) {
            timer.tma()
        } else if (address == 0xFF07) {
            timer.tac()
        } else if (address == 0xFF0F) {
            interrupts.ifFlag()
        } else if (address == 0xFF44) {
            0x90
        } else if (address == 0xFFFF) {
            interrupts.ieFlag()
        } else {
            data[address]
        }
    }

    override fun write8(address: Int, value: Int) {
        if (address == 0xFF01) {
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
        } else if (address == 0xFFFF) {
            interrupts.updateIeFlag(value)
        } else {
            data[address] = value
        }
    }
}