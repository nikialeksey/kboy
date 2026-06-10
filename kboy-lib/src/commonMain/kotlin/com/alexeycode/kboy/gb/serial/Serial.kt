package com.alexeycode.kboy.gb.serial

interface Serial {
    fun put(data: Int)

    class Dummy : Serial {
        override fun put(data: Int) {
            // ignored, since dummy
        }
    }
}