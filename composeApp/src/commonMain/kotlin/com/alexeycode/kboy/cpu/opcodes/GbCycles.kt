package com.alexeycode.kboy.cpu.opcodes

class GbCycles(
    private val action: Int,
    private val none: Int
) : Cycles {

    override fun action(): Int {
        return action
    }

    override fun none(): Int {
        return none
    }
}