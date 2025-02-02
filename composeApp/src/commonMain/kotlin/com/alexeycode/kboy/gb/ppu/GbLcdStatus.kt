package com.alexeycode.kboy.gb.ppu

class GbLcdStatus : LcdStatus {

    private var ly: Int = 0
    private var lyc: Int = 0
    private var stat: Int = 0

    override fun ly(): Int {
        return ly
    }

    override fun incrementLy() {
        ly++
    }

    override fun lyc(): Int {
        return lyc
    }

    override fun updateLyc(value: Int) {
        lyc = value
    }

    override fun stat(): Int {
        return stat
    }

    override fun updateStat(value: Int) {
        stat = stat.and(0b0000_0111).or(value.and(0b0111_1000))
    }
}