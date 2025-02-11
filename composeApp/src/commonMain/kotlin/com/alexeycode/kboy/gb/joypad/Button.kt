package com.alexeycode.kboy.gb.joypad

interface Button {
    fun press()
    fun release()
    fun line(): Int
    fun mask(): Int
}