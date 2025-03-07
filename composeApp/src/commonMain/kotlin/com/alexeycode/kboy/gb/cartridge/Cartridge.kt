package com.alexeycode.kboy.gb.cartridge

import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.Rom

interface Cartridge {

    fun memory(): Memory

    fun vram(): Memory

}