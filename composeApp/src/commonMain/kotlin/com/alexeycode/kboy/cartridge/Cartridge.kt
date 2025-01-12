package com.alexeycode.kboy.cartridge

import com.alexeycode.kboy.mem.Memory

interface Cartridge {
    fun upload(to: Memory)
}