package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory

class TraceableCpu(
    private val origin: Cpu,
    private val registers: Registers,
    private val memory: Memory,
    private val traceSize: Int = 50
) : Cpu {

    private val traces = Array(traceSize) { "" }

    override fun tick(): Int {
        val currentPc = registers.pc().get()

        val registerPart = "A:${registers.a()} " +
                "F:${registers.f()} " +
                "B:${registers.b()} " +
                "C:${registers.c()} " +
                "D:${registers.d()} " +
                "E:${registers.e()} " +
                "H:${registers.h()} " +
                "L:${registers.l()} " +
                "SP:${registers.sp()} " +
                "PC:${registers.pc()} "

        val trace = registerPart +
                "PCMEM:" +
                "${memory.read8(currentPc).hex()}," +
                "${memory.read8(currentPc + 1).hex()}," +
                "${memory.read8(currentPc + 2).hex()}," +
                "${memory.read8(currentPc + 3).hex()}"

        pushTrace(trace)

        return origin.tick()
    }

    fun traces(): Array<String> {
        return traces
    }

    private fun pushTrace(trace: String) {
        for (i in 0 until traceSize - 1) {
            traces[i] = traces[i+1]
        }
        traces[traceSize - 1] = trace
    }

    private fun Int.hex(): String {
        return this.toString(16).uppercase().padStart(2, padChar = '0')
    }
}