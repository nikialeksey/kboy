package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import java.io.Closeable
import java.io.PrintWriter

class DoctorGbCpu(
    private val origin: Cpu,
    private val registers: Registers,
    private val memory: Memory
) : Cpu, Closeable {

    private val logFile = PrintWriter("cpu_instrs.log")

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

        logFile.println(
            "$registerPart" +
                    "PCMEM:" +
                    "${memory.read8(currentPc).hex()}," +
                    "${memory.read8(currentPc+1).hex()}," +
                    "${memory.read8(currentPc+2).hex()}," +
                    "${memory.read8(currentPc+3).hex()}"
        )

        return origin.tick()
    }

    private fun Int.hex(): String {
        return this.toString(16).uppercase().padStart(2, padChar = '0')
    }

    override fun close() {
        logFile.close()
    }
}