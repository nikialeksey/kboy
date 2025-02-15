package com.alexeycode.kboy.gb.cpu.opcodes

interface Cycles {
    fun action(): Int
    fun none(): Int

    class Dummy : Cycles {
        override fun action(): Int {
            return 0
        }

        override fun none(): Int {
            return 0
        }

    }
}