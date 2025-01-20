package com.alexeycode.kboy.gb.cartridge

interface CartridgeMeta {
    fun header(): Header
    fun ramSizeKb(): Int
    fun romSizeKb(): Int
}