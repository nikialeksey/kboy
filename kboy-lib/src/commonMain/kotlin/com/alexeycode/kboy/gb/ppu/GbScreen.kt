package com.alexeycode.kboy.gb.ppu

import kotlin.concurrent.Volatile

class GbScreen(
    private val width: Int = SCREEN_WIDTH,
    private val height: Int = SCREEN_HEIGHT,
) : Screen {

    private val screenPixels = ByteArray(width * height * 4)
    @Volatile
    private var hash = 0

    override fun setPixel(x: Int, y: Int, r: Byte, g: Byte, b: Byte) {
        screenPixels[y * width * 4 + x * 4] = r
        screenPixels[y * width * 4 + x * 4 + 1] = g
        screenPixels[y * width * 4 + x * 4 + 2] = b
        screenPixels[y * width * 4 + x * 4 + 3] = 0xFF.toByte()
        hash++
    }

    override fun fill(r: Byte, g: Byte, b: Byte) {
        for (y in 0 until SCREEN_HEIGHT) {
            for (x in 0 until SCREEN_WIDTH) {
                setPixel(x, y, r, g, b)
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
