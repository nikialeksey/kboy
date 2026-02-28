package com.alexeycode.kboy.gb.mem

class GbDma(
    private var value: Int = 0xFF
) : Dma {

    private var isInProgress = false

    override fun value(): Int {
        return value
    }

    override fun updateValue(value: Int) {
        this.value = value
        isInProgress = true
    }

    override fun isTransferInProgress(): Boolean {
        return isInProgress
    }

    override fun transferFinished() {
        isInProgress = false
    }

}