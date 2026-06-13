package com.alexeycode.kboy.gb.cpu.registers

import com.alexeycode.kboy.gb.mem.toHexWord

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

    override fun toString(): String {
        return v.toHexWord()
    }
}
