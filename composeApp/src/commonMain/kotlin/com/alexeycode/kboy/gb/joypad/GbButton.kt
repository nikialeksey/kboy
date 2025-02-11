package com.alexeycode.kboy.gb.joypad

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts

class GbButton(
    private val interrupts: Interrupts,
    private val line: Int,
    private val mask: Int
) : Button {

    private var maskState: Int = 0

    override fun press() {
        maskState = maskState.or(mask)
        interrupts.requestJoypad()
    }

    override fun release() {
        maskState = 0
    }

    override fun line(): Int {
        return line
    }

    override fun mask(): Int {
        return maskState
    }
}