package com.alexeycode.kboy

import com.alexeycode.kboy.cpu.Cpu
import com.alexeycode.kboy.cpu.timer.Timer
import com.alexeycode.kboy.ppu.Ppu

class SimpleGb(
    private val timer: Timer,
    private val cpu: Cpu,
    private val ppu: Ppu
) : Gb {
    override fun run(cpuCycles: Int) {
        for (cycle in 0 until cpuCycles) {
            val clockCycles = cpu.tick()
            timer.tick(clockCycles)
            ppu.tick(clockCycles)
        }
    }
}