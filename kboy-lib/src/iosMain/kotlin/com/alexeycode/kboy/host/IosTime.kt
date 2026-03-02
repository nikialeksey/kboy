package com.alexeycode.kboy.host

import kotlin.time.Clock

class IosTime : Time {
    override fun currentTimeMs(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}