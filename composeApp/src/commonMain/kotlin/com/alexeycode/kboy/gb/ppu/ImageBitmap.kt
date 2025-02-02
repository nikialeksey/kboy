package com.alexeycode.kboy.gb.ppu

interface ImageBitmap {
    fun width(): Int
    fun height(): Int
    fun pixels(): ByteArray
}