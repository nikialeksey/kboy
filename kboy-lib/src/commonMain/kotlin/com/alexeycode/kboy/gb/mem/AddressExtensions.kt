package com.alexeycode.kboy.gb.mem

fun Int.toHexWord(): String {
    return toString(16).padStart(4, padChar = '0').uppercase()
}

fun Int.toHexByte(): String {
    return toString(16).padStart(2, '0')
}
