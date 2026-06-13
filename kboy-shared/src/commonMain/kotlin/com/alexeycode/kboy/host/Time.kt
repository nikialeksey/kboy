package com.alexeycode.kboy.host

interface Time {
    fun currentTimeMs(): Long

    class Dummy : Time {

        override fun currentTimeMs(): Long {
            return 0
        }
    }
}
