package pro.devdesign.gameboy.cpu

interface Cpu {
    fun executeNext(count: Int = 1)
}