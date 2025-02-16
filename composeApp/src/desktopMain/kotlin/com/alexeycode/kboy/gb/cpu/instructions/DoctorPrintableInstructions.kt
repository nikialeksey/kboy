package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import java.io.PrintWriter

class DoctorPrintableInstructions(
    private val origin: Instructions,
    private val registers: Registers,
    private val memory: Memory
) : Instructions {

    private val logFile = PrintWriter("cpu_instrs.log")

    override fun loadInstruction(address: Int) {
        origin.loadInstruction(address)

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
                    "${memory.read8(registers.pc().get()).hex()}," +
                    "${memory.read8(registers.pc().get()+1).hex()}," +
                    "${memory.read8(registers.pc().get()+2).hex()}," +
                    "${memory.read8(registers.pc().get()+3).hex()}"
        )
    }

    override fun nextAddress(): Int {
        return origin.nextAddress()
    }

    override fun isExtInstruction(): Boolean {
        return origin.isExtInstruction()
    }

    override fun instructionMeta(): InstructionMeta {
        return origin.instructionMeta()
    }

    private fun Int.hex(): String {
        return this.toString(16).uppercase().padStart(2, padChar = '0')
    }

}