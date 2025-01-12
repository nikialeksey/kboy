package com.alexeycode.kboy.cpu.registers.flags

import com.alexeycode.kboy.cpu.registers.Register

class SimpleFlagRegister : FlagRegister {

    private val z: Flag
    private val n: Flag
    private val h: Flag
    private val c: Flag

    constructor(f: Register) : this(
        z = SimpleFlag(f, 7),
        n = SimpleFlag(f, 6),
        h = SimpleFlag(f, 5),
        c = SimpleFlag(f, 4)
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

    override fun toString(): String {
        return "Z: ${z()}, N: ${n()}, H: ${h()}, C: ${c()}"
    }
}