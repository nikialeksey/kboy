package com.alexeycode.kboy.gb.ppu

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces

class GbImageBitmap : ImageBitmap {

    private val screenPixels = IntArray(160 * 144)

    override val colorSpace: ColorSpace
        get() = ColorSpaces.Srgb
    override val config: ImageBitmapConfig
        get() = ImageBitmapConfig.Argb8888
    override val hasAlpha: Boolean
        get() = false
    override val height: Int
        get() = 144
    override val width: Int
        get() = 160

    override fun prepareToDraw() = Unit

    override fun readPixels(
        buffer: IntArray,
        startX: Int,
        startY: Int,
        width: Int,
        height: Int,
        bufferOffset: Int,
        stride: Int
    ) {
        var x = startX
        var y = startY
        while (y < height) {
            while (x < width) {
                buffer[bufferOffset + y * width + x] = screenPixels[y * stride + x]
                x++
            }
            y++
            x = 0
        }
    }
}