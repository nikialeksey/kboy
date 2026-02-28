package com.alexeycode.kboy.gb.cpu.registers

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
        return v.toString(16).uppercase().padStart(4, padChar = '0')
    }
}