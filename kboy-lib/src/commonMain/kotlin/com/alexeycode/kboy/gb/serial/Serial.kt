package com.alexeycode.kboy.gb.serial

interface Serial {
    fun tick(clockCycles: Int)
    fun send(data: Int)
    fun get(): Int
    fun updateControl(control: Int)
    fun getControl(): Int

    class Dummy : Serial {

        override fun tick(clockCycles: Int) {
            // ignored
        }

        override fun send(data: Int) {
            // ignored, since dummy
        }

        override fun get(): Int {
            return 0
        }

        override fun updateControl(control: Int) {
            // ignored, since dummy
        }

        override fun getControl(): Int {
            return 0
        }
    }
}
