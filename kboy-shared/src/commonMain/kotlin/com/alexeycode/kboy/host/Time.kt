package com.alexeycode.kboy.host

interface Time {
    fun currentTimeMs(): Long

    class ProgressiveTime : Time {

        private var timeMs = 0L

        override fun currentTimeMs(): Long {
            timeMs += 100
            return timeMs
        }
    }
}