package com.alexeycode.kboy.gb.serial

interface OutputWire {
    fun send(data: Int)
    fun wasTransferred(): Boolean
    fun reset()
}
