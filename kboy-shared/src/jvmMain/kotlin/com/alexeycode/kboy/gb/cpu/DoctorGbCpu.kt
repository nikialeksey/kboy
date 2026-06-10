package com.alexeycode.kboy.gb.cpu

import java.io.Closeable
import java.io.PrintWriter

class DoctorGbCpu(
    private val origin: TraceableCpu
) : Cpu, Closeable {

    private val logFile = PrintWriter("cpu_instrs.log")

    override fun tick(): Int {
        logFile.println(origin.traces()[0])
        return origin.tick()
    }

    override fun close() {
        logFile.close()
    }
}
