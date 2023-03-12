package pro.devdesign.gameboy.mem

interface Memory : Rom {
    fun write8(address: Int, value: Int)
}