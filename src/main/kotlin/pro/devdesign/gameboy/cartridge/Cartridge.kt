package pro.devdesign.gameboy.cartridge

import pro.devdesign.gameboy.mem.Memory

interface Cartridge {
    fun upload(to: Memory)
}