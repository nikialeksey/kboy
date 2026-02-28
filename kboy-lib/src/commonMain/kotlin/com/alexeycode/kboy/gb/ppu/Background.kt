package com.alexeycode.kboy.gb.ppu

interface Background {

    fun scx(): Int
    fun updateScx(value: Int)

    fun scy(): Int
    fun updateScy(value: Int)
}