package com.alexeycode.kboy.host

import android.os.SystemClock

class AndroidTime : Time {
    override fun currentTimeMs(): Long {
        return SystemClock.uptimeMillis()
    }
}
