package com.alexeycode.kboy.gb.mem

import com.alexeycode.kboy.gb.cpu.registers.Registers

fun Memory.readNext8(r: Registers): Int {
    val result = read8(r.pc().get())

    r.pc().set(r.pc().get() + 1)

    return result
}

fun Memory.readNext16(r: Registers): Int {
    val p = r.pc().get()
    var result = 0
    var index = 0
    for (a in p until (p + 2)) {
        val part = read8(a)
        result += part * (1 shl (index * 8))
        index++
    }

    r.pc().set(r.pc().get() + 2)

    return result
}

fun Memory.readNextSigned8(r: Registers): Int {
    var result = read8(r.pc().get())
    r.pc().set(r.pc().get() + 1)

    if (result.and(0b1000_0000) != 0) {
        result -= 0b1_0000_0000
    }

    return result
}