package pro.devdesign.gameboy.cpu.instructions

import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory
import java.io.PrintWriter

class DoctorPrintableInstructions(
    private val origin: Instructions,
    private val registers: Registers,
    private val memory: Memory
) : Instructions {

    private val logFile = PrintWriter("cpu_instrs.log")

    override fun instruction(address: Int): ReadInstruction {
        val result = origin.instruction(address)

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

        return result
    }

    private fun flagRegister(): String {
        return "${if (registers.flag().c().isEnabled()) "C" else "-"}" +
                "${if (registers.flag().h().isEnabled()) "H" else "-"}" +
                "${if (registers.flag().n().isEnabled()) "N" else "-"}" +
                "${if (registers.flag().z().isEnabled()) "Z" else "-"}"
    }

    private fun Int.hex(): String {
        return this.toString(16).uppercase().padStart(2, padChar = '0')
    }

}