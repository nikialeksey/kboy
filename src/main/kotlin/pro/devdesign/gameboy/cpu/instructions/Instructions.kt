package pro.devdesign.gameboy.cpu.instructions

interface Instructions {
    fun instruction(address: Int): ReadInstruction
}