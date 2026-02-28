package com.alexeycode.kboy.host

interface Vibrator {
    fun vibrate()

    class Dummy : Vibrator {
        override fun vibrate() {
            // ignore
        }
    }
}