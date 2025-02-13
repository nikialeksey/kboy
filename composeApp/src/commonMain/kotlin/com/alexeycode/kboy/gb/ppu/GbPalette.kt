package com.alexeycode.kboy.gb.ppu

class GbPalette(
    private var bgp: Int = 0xFC,
    private var obp0: Int = 0x00,
    private var obp1: Int = 0x00
) : Palette {

    override fun getBgp(): Int {
        return bgp
    }

    override fun updateBgp(value: Int) {
        bgp = value
    }

    override fun getObp0(): Int {
        return obp0
    }

    override fun updateObp0(value: Int) {
        obp0 = value
    }

    override fun getObp1(): Int {
        return obp1
    }

    override fun updateObp1(value: Int) {
        obp1 = value
    }

    override fun drawBgpPixel(
        screen: Screen,
        x: Int,
        y: Int,
        paletteData: Int
    ) {
        if (paletteData == 0) {
            draw(screen, x, y, bgp.and(0b0000_0011))
        } else if (paletteData == 1) {
            draw(screen, x, y, bgp.and(0b0000_1100).shr(2))
        } else if (paletteData == 2) {
            draw(screen, x, y, bgp.and(0b0011_0000).shr(4))
        } else if (paletteData == 3) {
            draw(screen, x, y, bgp.and(0b1100_0000).shr(6))
        }
    }

    override fun drawObp0Pixel(
        screen: Screen,
        x: Int,
        y: Int,
        paletteData: Int
    ) {
        if (paletteData == 0) {
            // transparent
        } else if (paletteData == 1) {
            draw(screen, x, y, obp0.and(0b0000_1100).shr(2))
        } else if (paletteData == 2) {
            draw(screen, x, y, obp0.and(0b0011_0000).shr(4))
        } else if (paletteData == 3) {
            draw(screen, x, y, obp0.and(0b1100_0000).shr(6))
        }
    }

    override fun drawObp1Pixel(
        screen: Screen,
        x: Int,
        y: Int,
        paletteData: Int
    ) {
        if (paletteData == 0) {
            // transparent
        } else if (paletteData == 1) {
            draw(screen, x, y, obp1.and(0b0000_1100).shr(2))
        } else if (paletteData == 2) {
            draw(screen, x, y, obp1.and(0b0011_0000).shr(4))
        } else if (paletteData == 3) {
            draw(screen, x, y, obp1.and(0b1100_0000).shr(6))
        }
    }

    private fun draw(
        screen: Screen,
        x: Int,
        y: Int,
        pixelData: Int
    ) {
        if (pixelData == 0) {
            screen.setPixel(
                x,
                y,
                0xEE.toByte(),
                0xEE.toByte(),
                0xEE.toByte()
            )
        } else if (pixelData == 1) {
            screen.setPixel(
                x,
                y,
                0xAA.toByte(),
                0xAA.toByte(),
                0xAA.toByte()
            )
        } else if (pixelData == 2) {
            screen.setPixel(
                x,
                y,
                0x66.toByte(),
                0x66.toByte(),
                0x66.toByte()
            )
        } else if (pixelData == 3) {
            screen.setPixel(
                x,
                y,
                0x22.toByte(),
                0x22.toByte(),
                0x22.toByte()
            )
        }
    }
}