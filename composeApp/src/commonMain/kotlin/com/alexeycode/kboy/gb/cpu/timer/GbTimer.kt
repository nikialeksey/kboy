package com.alexeycode.kboy.gb.cpu.timer

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts

/**
 * https://gbdev.io/pandocs/Timer_and_Divider_Registers.html
 * https://hacktix.github.io/GBEDG/timers/#-ff04---divider-register--div-
 * https://gbdev.io/pandocs/Timer_Obscure_Behaviour.html#relation-between-timer-and-divider-register
 */
class GbTimer(
    private val interrupts: Interrupts,
    private var div: Int = 0x0018,
    private var tma: Int = 0x00,
    private var tac: Int = 0xF8,
    private var tima: Int = 0x00,
) : Timer {

    private var nextTma: Int = -1
    private var overflow: Boolean = false
    private var cyclesSinceOverflow: Int = 0

    override fun tick(clockCycles: Int) {
        val previousDiv = div
        incrementDiv(clockCycles)
        incrementTima(previousDiv, div, clockCycles)
        if (nextTma != -1) {
            tma = nextTma
            nextTma = -1
        }
    }

    override fun div(): Int {
        return div.shr(8)
    }

    override fun resetDiv() {
        div = 0
    }

    override fun tima(): Int {
        return tima.and(0xFF)
    }

    override fun updateTima(tima: Int) {
        if (cyclesSinceOverflow < 4) {
            this.tima = tima
            overflow = false
            cyclesSinceOverflow = 0
        }
    }

    override fun tma(): Int {
        return tma.and(0xFF)
    }

    override fun updateTma(tma: Int) {
        nextTma = tma.and(0xFF)
    }

    override fun tac(): Int {
        return tac.or(0xF8)
    }

    override fun updateTac(tac: Int) {
        this.tac = tac.or(0xF8)
    }

    private fun incrementDiv(clockCycles: Int) {
        div = (div + clockCycles) and 0xFFFF
    }

    private fun incrementTima(previousDiv: Int, div: Int, clockCycles: Int) {
        val enabled = (tac and 1.shl(2)) != 0
        if (enabled) {
            if (overflow) {
                cyclesSinceOverflow += clockCycles
                if (cyclesSinceOverflow >= 4) {
                    interrupts.requestTimer()
                    tima = tma
                    overflow = false
                    cyclesSinceOverflow = 0
                }
            } else {
                val tacBits = tac and 0b11
                val bit = if (tacBits == 0b00) {
                    9
                } else if (tacBits == 0b01) {
                    3
                } else if (tacBits == 0b10) {
                    5
                } else /*if (tacBits == 0b11)*/ {
                    7
                }
                var previousBitEnabled = false
                for (n in previousDiv .. div) {
                    if (n and (1.shl(bit)) != 0) {
                        previousBitEnabled = true
                        break
                    }
                }
                val newBitDisabled = div and (1.shl(bit)) == 0
                if (previousBitEnabled && newBitDisabled) {
                    tima++
                    if (tima > 0xFF) {
                        tima = 0x00
                        overflow = true
                    }
                }
            }
        }
    }
}