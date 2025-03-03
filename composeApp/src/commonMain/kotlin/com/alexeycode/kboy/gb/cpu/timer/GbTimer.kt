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
        val beforeDiv = div
        div = (div + clockCycles) and 0xFFFF
        val afterDiv = div

        incrementTima(beforeDiv, afterDiv, clockCycles)
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

    private fun incrementTima(previousDiv: Int, div: Int, clockCycles: Int) {
        val enabled = (tac and 1.shl(2)) != 0
        if (enabled) {
            if (overflow) {
                cyclesSinceOverflow += clockCycles
                requestTimerIfNeeded()
            } else {
                val tacBits = tac and 0b11
                val divider = when (tacBits) {
                    0b00 -> {
                        1024
                    }
                    0b01 -> {
                        16
                    }
                    0b10 -> {
                        64
                    }
                    else -> /*if (tacBits == 0b11)*/ {
                        256
                    }
                }

                val timaDiff = div / divider - previousDiv / divider
                tima += timaDiff
                if (tima > 0xFF) {
                    overflow = true
                    cyclesSinceOverflow = tima - 0xFF - 1
                    tima = 0x00
                    requestTimerIfNeeded()
                }
            }
        }
    }

    private fun requestTimerIfNeeded() {
        if (cyclesSinceOverflow >= 4) {
            interrupts.requestTimer()
            tima = tma
            overflow = false
            cyclesSinceOverflow = 0
        }
    }
}