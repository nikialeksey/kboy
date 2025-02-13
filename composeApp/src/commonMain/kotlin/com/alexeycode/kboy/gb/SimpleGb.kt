package com.alexeycode.kboy.gb

import com.alexeycode.kboy.gb.cpu.Cpu
import com.alexeycode.kboy.gb.cpu.timer.Timer
import com.alexeycode.kboy.gb.mem.DmaTransfer
import com.alexeycode.kboy.gb.ppu.Ppu

class SimpleGb(
    private val timer: Timer,
    private val cpu: Cpu,
    private val dma: DmaTransfer,
    private val ppu: Ppu
) : Gb {
    override fun run(cpuCycles: Int) {
        for (cycle in 0 until cpuCycles) {
            val clockCycles = cpu.tick()
            timer.tick(clockCycles)
            dma.tick(clockCycles)
            ppu.tick(clockCycles)
        }
    }
}