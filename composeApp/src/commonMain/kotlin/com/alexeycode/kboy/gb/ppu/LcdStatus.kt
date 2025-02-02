package com.alexeycode.kboy.gb.ppu

interface LcdStatus {
    fun ly(): Int
    fun incrementLy()

    fun lyc(): Int
    fun updateLyc(value: Int)

    fun stat(): Int
    fun updateStat(value: Int)
}