package com.alexeycode.kboy.gb.ppu

class GbWindow(
    private var wx: Int = 0x00,
    private var wy: Int = 0x00
) : Window {

    override fun wx(): Int {
        return wx
    }

    override fun updateWx(value: Int) {
        wx = value
    }

    override fun wy(): Int {
        return wy
    }

    override fun updateWy(value: Int) {
        wy = value
    }
}