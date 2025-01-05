package pro.devdesign.gameboy.cpu.opcodes

interface Cycles {
    fun action(): Int
    fun none(): Int
}