package com.alexeycode.kboy.gb.ppu

class GbLcdStatus : LcdStatus {

    private var ly: Int = 0
    private var lyc: Int = 0
    private var lyEqualsLyc: Boolean = false
    private var stat: Int = 0
    private var oamScanStarted = false
    private var drawingStarted = false
    private var hBlankStarted = false
    private var vBlankStarted = false

    override fun ly(): Int {
        return ly
    }

    override fun updateLy(currentLy: Int) {
        ly = currentLy
    }

    override fun lyc(): Int {
        return lyc
    }

    override fun updateLyc(value: Int) {
        lyc = value
    }

    override fun updateLyLyc(equals: Boolean) {
        lyEqualsLyc = equals
    }

    override fun stat(): Int {
        val mode = if (hBlankStarted) {
            0b00
        } else if (vBlankStarted) {
            0b01
        } else if (oamScanStarted) {
            0b10
        } else /* if (drawingStarted) */ {
            0b11
        }
        val lyLyc = if (lyEqualsLyc) {
            0b0100
        } else {
            0
        }
        return stat// or mode or lyLyc
    }

    override fun updateStat(value: Int) {
        stat = stat.and(0b0000_0111).or(value.and(0b0111_1000))
    }

    override fun isDrawing(): Boolean {
        return drawingStarted
    }

    override fun drawingStarted() {
        drawingStarted = true
    }

    override fun drawingFinished() {
        drawingStarted = false
    }

    override fun isHBlank(): Boolean {
        return hBlankStarted
    }

    override fun hBlankStarted() {
        hBlankStarted = true
    }

    override fun hBlankFinished() {
        hBlankStarted = false
    }

    override fun isVBlank(): Boolean {
        return vBlankStarted
    }

    override fun vBlankStarted() {
        vBlankStarted = true
    }

    override fun vBlankFinished() {
        vBlankStarted = false
    }

    override fun isOamScan(): Boolean {
        return oamScanStarted
    }

    override fun oamScanStarted() {
        oamScanStarted = true
    }

    override fun oamScanFinished() {
        oamScanStarted = false
    }
}