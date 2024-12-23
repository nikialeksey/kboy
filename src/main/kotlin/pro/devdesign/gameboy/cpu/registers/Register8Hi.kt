package pro.devdesign.gameboy.cpu.registers

class Register8Hi(
    private val origin: Register
) : Register {
    override fun bytes(): Int {
        return origin.bytes()
    }

    override fun set(v: Int) {
        origin.set(v and 0xF0)
    }

    override fun get(): Int {
        return origin.get() and 0xF0
    }

    override fun toString(): String {
        return origin.toString()
    }
}