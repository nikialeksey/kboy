package com.alexeycode.kboy.gb.cartridge

interface Header {
    fun logo(): Logo
    fun name(): String
}