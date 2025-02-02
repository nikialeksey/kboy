package com.alexeycode.kboy.gb.ppu

import kotlin.concurrent.Volatile

class GbImageBitmap(
    private val width: Int = 160,
    private val height: Int = 144,
) : ImageBitmap {

    private val screenPixels = ByteArray(width * height * 4)
    @Volatile
    private var hash = 0

    fun setPixel(x: Int, y: Int, r: Byte, g: Byte, b: Byte, alpha: Byte) {
        screenPixels[y * width * 4 + x * 4] = r
        screenPixels[y * width * 4 + x * 4 + 1] = g
        screenPixels[y * width * 4 + x * 4 + 2] = b
        screenPixels[y * width * 4 + x * 4 + 3] = alpha
        hash++
    }

    override fun width(): Int {
        return width
    }

    override fun height(): Int {
        return height
    }

    override fun pixels(): ByteArray {
        return screenPixels
    }

    override fun hashCode(): Int {
        return hash
    }

    override fun equals(other: Any?): Boolean {
        return false
    }
}
