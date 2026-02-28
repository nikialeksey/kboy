package com.alexeycode.kboy.gb.cpu.interrupts

interface Interrupts {
    fun enable()
    fun disable()

    fun ieFlag(): Int
    fun updateIeFlag(flag: Int)

    fun ifFlag(): Int
    fun updateIfFlag(flag: Int)

    fun requestVBlank()
    fun requestLcd()
    fun requestTimer()
    fun requestJoypad()

    fun tryRun(): Int
}