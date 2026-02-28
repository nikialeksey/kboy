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
                0xFB.toByte(),
                0xF9.toByte(),
                0xFB.toByte()
            )
        } else if (pixelData == 1) {
            screen.setPixel(
                x,
                y,
                0xEB.toByte(),
                0x99.toByte(),
                0x55.toByte()
            )
        } else if (pixelData == 2) {
            screen.setPixel(
                x,
                y,
                0x83.toByte(),
                0x41.toByte(),
                0x06.toByte()
            )
        } else if (pixelData == 3) {
            screen.setPixel(
                x,
                y,
                0x04.toByte(),
                0x02.toByte(),
                0x04.toByte()
            )
        }
    }
}