package pro.devdesign.gameboy.cpu

interface Cpu {
    fun execute(cpuCycles: Int = 1)
}