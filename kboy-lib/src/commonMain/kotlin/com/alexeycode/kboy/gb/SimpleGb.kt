package com.alexeycode.kboy.gb

import com.alexeycode.kboy.gb.cpu.Cpu
import com.alexeycode.kboy.gb.cpu.timer.Timer
import com.alexeycode.kboy.gb.mem.DmaTransfer
import com.alexeycode.kboy.gb.ppu.Ppu
import com.alexeycode.kboy.gb.serial.Serial

class SimpleGb(
    private val timer: Timer,
    private val cpu: Cpu,
    private val dma: DmaTransfer,
    private val ppu: Ppu,
    private val serial: Serial,
) : Gb {
    @Suppress("UnusedPrivateProperty")
    override fun run(cpuCycles: Int): Int {
        var clockCyclesSpent = 0
        for (cycle in 0 until cpuCycles) {
            val clockCycles = cpu.tick()
            timer.tick(clockCycles)
            dma.tick(clockCycles)
            ppu.tick(clockCycles)
            serial.tick(clockCycles)
            clockCyclesSpent += clockCycles
        }
        return clockCyclesSpent
    }
}
