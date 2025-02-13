package com.alexeycode.kboy.gb.mem

interface DmaTransfer {
    fun tick(clockCycles: Int)
}