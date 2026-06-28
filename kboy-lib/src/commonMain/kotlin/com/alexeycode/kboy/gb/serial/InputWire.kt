package com.alexeycode.kboy.gb.serial

interface InputWire {
    fun wasReceived(): Boolean
    fun get(): Int
    fun reset()

    class Dummy : InputWire {
        override fun wasReceived(): Boolean {
            return false
        }

        override fun get(): Int {
            return 0
        }

        override fun reset() {
            // ignored since dummy
        }
    }
}
