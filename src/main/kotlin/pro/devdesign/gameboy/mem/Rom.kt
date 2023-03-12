package pro.devdesign.gameboy.mem

interface Rom {
    fun read8(address: Int): Int
}