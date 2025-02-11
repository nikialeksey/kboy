package com.alexeycode.kboy.gb.ppu

class GbBackground(
    private var scx: Int = 0x00,
    private var scy: Int = 0x00
) : Background {

    override fun scx(): Int {
        return scx
    }

    override fun updateScx(value: Int) {
        scx = value
    }

    override fun scy(): Int {
        return scy
    }

    override fun updateScy(value: Int) {
        scy = value
    }
}