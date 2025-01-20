package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Memory

interface Cartridge {
    fun upload(to: Memory)
}