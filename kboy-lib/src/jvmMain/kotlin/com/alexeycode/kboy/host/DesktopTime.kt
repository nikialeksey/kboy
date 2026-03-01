package com.alexeycode.kboy.host

class DesktopTime : Time {
    override fun currentTimeMs(): Long {
        return System.currentTimeMillis()
    }
}
