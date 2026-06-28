package com.alexeycode.kboy.gb.serial

interface Serial {
    fun tick(clockCycles: Int)
    fun send(data: Int)
    fun get(): Int
    fun updateControl(control: Int)
    fun getControl(): Int
}
