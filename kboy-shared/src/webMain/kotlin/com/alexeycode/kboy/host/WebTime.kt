package com.alexeycode.kboy.host

import org.w3c.performance.Performance

class WebTime(
    private val performance: Performance
) : Time {
    override fun currentTimeMs(): Long {
        return performance.now().toLong()
    }
}