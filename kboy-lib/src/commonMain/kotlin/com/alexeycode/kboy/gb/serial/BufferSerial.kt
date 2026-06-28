package com.alexeycode.kboy.gb.serial

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts

class BufferSerial(
    private val interrupts: Interrupts
) : Serial {

    private val buffer = mutableListOf<Int>()
    private var control: Int = 0b0000_0010

    private var currentCycles: Int = 0
    private var transferStartedCycles: Int = 0

    override fun tick(clockCycles: Int) {
        currentCycles += clockCycles
        if (control.isTransfer() && (currentCycles - transferStartedCycles > 0)) {
            control = control.and(0b0111_1111)
            interrupts.requestSerial()
        }
    }

    override fun send(data: Int) {
        if (!control.isTransfer()) {
            buffer.add(data)
        }
    }

    override fun get(): Int {
        return buffer.lastOrNull() ?: 0
    }

    override fun updateControl(control: Int) {
        val updatedControl = control.and(0b1000_0001).or(0b0000_0010)
        if (!this.control.isTransfer() && updatedControl.isTransfer()) {
            transferStartedCycles = currentCycles
        }

        this.control = updatedControl
    }

    override fun getControl(): Int {
        return this.control
    }

    fun asByteArray(): ByteArray {
        return ByteArray(buffer.size) { i -> buffer[i].toByte() }
    }

    private fun Int.isTransfer(): Boolean {
        return this.and(0b1000_0000) != 0
    }
}
