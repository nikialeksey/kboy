package pro.devdesign.gameboy.cpu.registers.flags

interface FlagRegister {
    fun z(): Flag
    fun n(): Flag
    fun h(): Flag
    fun c(): Flag
}