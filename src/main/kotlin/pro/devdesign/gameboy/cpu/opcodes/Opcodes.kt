package pro.devdesign.gameboy.cpu.opcodes

interface Opcodes {
    fun instructionMeta(code: Int): InstructionMeta
    fun cbInstructionMeta(code: Int): InstructionMeta
}