package com.alexeycode.kboy.gb.ppu

interface LcdControl {
    fun update(value: Int)
    fun get(): Int

    fun lcdEnable(): Boolean

    fun windowEnable(): Boolean
    fun windowTileMapStart(): Int

    fun objEnable(): Boolean
    fun objHeight(): Int

    fun bgAndWindowEnable(): Boolean
    fun bgAndWindowTileDataStart(): Int
    fun bgAndWindowTileDataSignedAddressing(): Boolean

    fun bgTileMapStart(): Int
}