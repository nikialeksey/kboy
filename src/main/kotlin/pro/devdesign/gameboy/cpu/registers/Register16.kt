package pro.devdesign.gameboy.cpu.registers

class Register16 : Register {
    private var v: Int

    constructor() : this(0)

    constructor(v: Int) {
        this.v = v
    }

    override fun bytes(): Int {
        return 2
    }

    override fun set(v: Int) {
        this.v = v.and(0xFFFF)
    }

    override fun get(): Int {
        return v
    }
}