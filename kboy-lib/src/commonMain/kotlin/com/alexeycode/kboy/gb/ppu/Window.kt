package com.alexeycode.kboy.gb.ppu

interface Window {

    fun wx(): Int
    fun updateWx(value: Int)

    fun wy(): Int
    fun updateWy(value: Int)
}