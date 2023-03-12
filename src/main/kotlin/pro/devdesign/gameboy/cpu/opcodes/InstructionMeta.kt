package pro.devdesign.gameboy.cpu.opcodes

interface InstructionMeta {
    fun opcode(): Int
    fun isImmediate(): Boolean
    fun cycles(): List<Int>
    fun bytes(): Int
    fun mnemonic(): String
    fun operands(): List<OperandMeta>
    fun flags(): Map<String, String>
}