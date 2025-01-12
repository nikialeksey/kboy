package com.alexeycode.kboy.cartridge

interface CartridgeMeta {
    fun header(): Header
    fun ramSizeKb(): Int
    fun romSizeKb(): Int
}