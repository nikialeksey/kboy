package pro.devdesign.gameboy.cpu.registers.flags

class SimpleFlagRegister : FlagRegister {

    private val z: Flag
    private val n: Flag
    private val h: Flag
    private val c: Flag

    constructor() : this(
        SimpleFlag(),
        SimpleFlag(),
        SimpleFlag(),
        SimpleFlag()
    )

    constructor(
        z: Flag,
        n: Flag,
        h: Flag,
        c: Flag
    ) {
        this.z = z
        this.n = n
        this.h = h
        this.c = c
    }

    override fun z(): Flag {
        return z
    }

    override fun n(): Flag {
        return n
    }

    override fun h(): Flag {
        return h
    }

    override fun c(): Flag {
        return c
    }
}