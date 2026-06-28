package com.alexeycode.kboy.gb.serial

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts

class GbSerial(
    private val interrupts: Interrupts,
    private val outputWire: OutputWire,
    private val inputWire: InputWire,
) : Serial {

    private var buffer: Int = 0
    private var control: Int = 0b0000_0010

    private var currentCycles: Int = 0

    override fun tick(clockCycles: Int) {
        currentCycles += clockCycles
        if (control.isTransfer()) {
            if (control.isMaster()) {
                if (outputWire.wasTransferred()) {
                    outputWire.reset()
                    control = control.and(0b0111_1111)
                    interrupts.requestSerial()
                }
            } else {
                if (inputWire.wasReceived()) {
                    buffer = inputWire.get()
                    inputWire.reset()
                    control = control.and(0b0111_1111)
                    interrupts.requestSerial() // ?
                }
            }
        }
    }

    override fun send(data: Int) {
        if (!control.isTransfer()) {
            buffer = data
        }
    }

    override fun get(): Int {
        return buffer
    }

    override fun updateControl(control: Int) {
        val updatedControl = control.and(0b1000_0001).or(0b0000_0010)
        if (!this.control.isTransfer() && updatedControl.isTransfer()) {
            if (updatedControl.isMaster()) {
                outputWire.send(buffer)
            } else {
                // nothing to do here, just waiting for data to be received
            }
        }

        this.control = updatedControl
    }

    override fun getControl(): Int {
        return this.control
    }

    private fun Int.isTransfer(): Boolean {
        return this.and(0b1000_0000) != 0
    }

    private fun Int.isMaster(): Boolean {
        return this.and(0b0000_0001) != 0
    }
}
