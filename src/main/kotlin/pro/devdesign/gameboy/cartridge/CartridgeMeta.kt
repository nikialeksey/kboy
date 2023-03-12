package pro.devdesign.gameboy.cartridge

interface CartridgeMeta {
    fun header(): Header
    fun ramSizeKb(): Int
    fun romSizeKb(): Int
}