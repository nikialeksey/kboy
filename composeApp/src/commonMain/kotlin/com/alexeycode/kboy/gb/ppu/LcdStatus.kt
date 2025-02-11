package com.alexeycode.kboy.gb.ppu

interface LcdStatus {
    fun ly(): Int
    fun updateLy(currentLy: Int)

    fun lyc(): Int
    fun updateLyc(value: Int)

    fun stat(): Int
    fun updateStat(value: Int)
}