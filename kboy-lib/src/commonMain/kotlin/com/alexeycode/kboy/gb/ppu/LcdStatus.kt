package com.alexeycode.kboy.gb.ppu

interface LcdStatus {
    fun ly(): Int
    fun updateLy(currentLy: Int)

    fun lyc(): Int
    fun updateLyc(value: Int)
    fun updateLyLyc(equals: Boolean)

    fun stat(): Int
    fun updateStat(value: Int)

    fun isOamScan(): Boolean
    fun oamScanStarted()
    fun oamScanFinished()

    fun isDrawing(): Boolean
    fun drawingStarted()
    fun drawingFinished()

    fun isHBlank(): Boolean
    fun hBlankStarted()
    fun hBlankFinished()

    fun isVBlank(): Boolean
    fun vBlankStarted()
    fun vBlankFinished()
}
