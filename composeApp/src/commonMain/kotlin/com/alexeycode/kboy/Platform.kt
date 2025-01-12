package com.alexeycode.kboy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform