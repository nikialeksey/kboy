package com.alexeycode.kboy.cpu.instructions.address

class SimpleAddress(
    private val address: Int
) : Address {

    private val hex by lazy {
        address.toString(16).uppercase().padStart(4, padChar = '0')
    }

    override fun asInt(): Int {
        return address
    }

    override fun toString(): String {
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

        val prefix = if (address in (0x0000..0x3FFF)) {
            "ROM0"
        } else if (address in (0x4000..0x7FFF)) {
            "ROM1"
        } else if (address in (0x8000..0x9FFF)) {
            "VRA0"
        } else if (address in (0xA000..0xBFFF)) {
            "SRA0"
        } else if (address in (0xC000..0xCFFF)) {
            "WRA0"
        } else if (address in (0xD000..0xDFFF)) {
            "WRA1"
        } else if (address in (0xE000..0xEFFF)) {
            "ECH0"
        } else if (address in (0xF000..0xFDFF)) {
            "ECH1"
        } else if (address in (0xFE00..0xFE9F)) {
            "OAM "
        } else if (address in (0xFEA0..0xFEFF)) {
            "----"
        } else if (address in (0xFF00..0xFF7F) || address == 0xFFFF) {
            "I/O "
        } else if (address in (0xFF80..0xFFFE)) {
            "HRAM"
        } else {
            throw IllegalArgumentException("Unknown address: $hex (decimal: $address)")
        }
        return "$prefix:$hex"
    }
}