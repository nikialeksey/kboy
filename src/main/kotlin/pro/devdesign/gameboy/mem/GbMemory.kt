package pro.devdesign.gameboy.mem

import pro.devdesign.gameboy.serial.Serial

class GbMemory : Memory {

    private val data: Array<Int>
    private val serial: Serial

    constructor(serial: Serial) : this(0xFFFF + 1, serial)

    constructor(size: Int, serial: Serial) : this(Array(size) { 0 }, serial)

    constructor(data: Array<Int>, serial: Serial) {
        this.data = data
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
        if (address == 0xFF02) {
            println("Read from control")
        }
        if (address == 0xFF44) {
            return 0x90
        }
        return data[address]
    }

    override fun write8(address: Int, value: Int) {
        if (address == 0xFFFF) {
            println("Enable interrupt: 0x${value.toString(16).uppercase()}")
        }
        if (address == 0xFF0F) {
            println("Request interrupt: 0x${value.toString(16).uppercase()}")
        }
        if (address == 0xFF01) {
            println("Data: 0x${value.toString(16).uppercase()} (dec: $value, char: ${value.toChar()})")
            serial.put(value)
        }
        if (address == 0xFF02) {
            // println("Control: 0x${value.toString(16).uppercase()}")
        }

        data[address] = value
    }
}