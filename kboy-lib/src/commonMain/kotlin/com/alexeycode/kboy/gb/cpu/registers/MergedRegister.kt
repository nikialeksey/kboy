package com.alexeycode.kboy.gb.cpu.registers

import com.alexeycode.kboy.gb.mem.toHexWord

class MergedRegister : Register {

    private val left: Register
    private val right: Register

    constructor(left: Register, right: Register) {
        this.left = left
        this.right = right
    }

    override fun set(v: Int) {
        left.set(v.and(0xFFFF).shr(8))
        right.set(v.and(0xFF))
    }

    override fun get(): Int {
        return left.get().shl(8) + right.get()
    }

    override fun toString(): String {
        return get().toHexWord()
    }
}
