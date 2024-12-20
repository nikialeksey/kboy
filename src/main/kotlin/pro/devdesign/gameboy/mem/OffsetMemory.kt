package pro.devdesign.gameboy.mem

class OffsetMemory(
    private val origin: Memory,
    private val offset: Int
) : Memory {
    override fun write8(address: Int, value: Int) {
        origin.write8(address + offset, value)
    }

    override fun read8(address: Int): Int {
        return origin.read8(address + offset)
    }
}