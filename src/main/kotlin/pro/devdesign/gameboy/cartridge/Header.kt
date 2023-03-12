package pro.devdesign.gameboy.cartridge

interface Header {
    fun logo(): Logo
    fun name(): String
}