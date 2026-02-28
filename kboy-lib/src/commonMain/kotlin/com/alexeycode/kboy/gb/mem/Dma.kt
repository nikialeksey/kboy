package com.alexeycode.kboy.gb.mem

interface Dma {
    fun value(): Int
    fun updateValue(value: Int)
    fun isTransferInProgress(): Boolean
    fun transferFinished()
}