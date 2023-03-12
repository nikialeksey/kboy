package pro.devdesign.gameboy.cpu.registers

class Register8 : Register {

    private var v: Int

    constructor() : this(0)

    constructor(v: Int) {
        this.v = v
    }

    override fun bytes(): Int {
        return 1
    }

    override fun set(v: Int) {
        this.v = v.and(0xFF)
    }

    override fun get(): Int {
        return v
    }
}