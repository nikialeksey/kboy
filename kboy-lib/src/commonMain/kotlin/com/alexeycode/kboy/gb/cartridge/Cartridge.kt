package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Memory

interface Cartridge {

    fun memory(): Memory

    fun vram(): Memory
}
