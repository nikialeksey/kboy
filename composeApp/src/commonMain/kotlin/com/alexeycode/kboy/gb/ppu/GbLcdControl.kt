package com.alexeycode.kboy.gb.ppu

class GbLcdControl(
    private var value: Int = 0x91
) : LcdControl {

    override fun update(value: Int) {
        this.value = value
    }

    override fun get(): Int {
        return value
    }

    override fun lcdEnable(): Boolean {
        return value.and(1.shl(7)) != 0
    }

    override fun windowEnable(): Boolean {
        return value.and(1.shl(5)) != 0
    }

    override fun windowTileMapStart(): Int {
        return if (value.and(1.shl(6)) != 0) {
            0x9C00
        } else {
            0x9800
        }
    }

    override fun objEnable(): Boolean {
        return value.and(1.shl(1)) != 0
    }

    override fun objHeight(): Int {
        return if (value.and(1.shl(2)) == 0) {
            8
        } else {
            16
        }
    }

    override fun bgAndWindowEnable(): Boolean {
        return value.and(1.shl(0)) != 0
    }

    override fun bgAndWindowTileDataStart(): Int {
        return if (value.and(1.shl(4)) != 0) {
            0x8000
        } else {
            0x9000
        }
    }

    override fun bgAndWindowTileDataSignedAddressing(): Boolean {
        return if (value.and(1.shl(4)) != 0) {
            false
        } else {
            true
        }
    }

    override fun bgTileMapStart(): Int {
        return if (value.and(1.shl(3)) != 0) {
            0x9C00
        } else {
            0x9800
        }
    }
}