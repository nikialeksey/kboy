package com.alexeycode.kboy.gb.ppu

interface Palette {
    fun getBgp(): Int
    fun updateBgp(value: Int)

    fun getObp0(): Int
    fun updateObp0(value: Int)

    fun getObp1(): Int
    fun updateObp1(value: Int)

    fun drawBgpPixel(screen: Screen, x: Int, y: Int, paletteData: Int)
    fun drawObp0Pixel(screen: Screen, x: Int, y: Int, paletteData: Int)
    fun drawObp1Pixel(screen: Screen, x: Int, y: Int, paletteData: Int)
}