package com.alexeycode.kboy.gb.ppu

import kotlin.concurrent.Volatile

class GbImageBitmap(
    private val width: Int = SCREEN_WIDTH,
    private val height: Int = SCREEN_HEIGHT,
) : ImageBitmap {

    private val screenPixels = ByteArray(width * height * 4)
    @Volatile
    private var hash = 0

    fun setPixel(x: Int, y: Int, r: Byte, g: Byte, b: Byte, alpha: Byte = 0xFF.toByte()) {
        screenPixels[y * width * 4 + x * 4] = r
        screenPixels[y * width * 4 + x * 4 + 1] = g
        screenPixels[y * width * 4 + x * 4 + 2] = b
        screenPixels[y * width * 4 + x * 4 + 3] = alpha
        hash++
    }

    fun fill(r: Byte, g: Byte, b: Byte, alpha: Byte = 0xFF.toByte()) {
        for (y in 0 until SCREEN_HEIGHT) {
            for (x in 0 until SCREEN_WIDTH) {
                setPixel(x, y, r, g, b, alpha)
            }
        }
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
