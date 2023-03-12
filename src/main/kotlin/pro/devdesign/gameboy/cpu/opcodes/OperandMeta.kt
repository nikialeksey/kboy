package pro.devdesign.gameboy.cpu.opcodes

interface OperandMeta {
    fun isImmediate(): Boolean
    fun name(): String
    fun bytes(): Int
    fun sign(): Int
}