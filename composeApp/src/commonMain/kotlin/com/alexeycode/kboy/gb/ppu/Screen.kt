package com.alexeycode.kboy.gb.ppu

interface Screen {
    fun width(): Int
    fun height(): Int
    fun pixels(): ByteArray
    fun setPixel(x: Int, y: Int, r: Byte, g: Byte, b: Byte)
    fun fill(r: Byte, g: Byte, b: Byte)
}