package pro.devdesign.gameboy.cpu.opcodes

class EmptyInstructionMeta : InstructionMeta {
    override fun opcode(): Int {
        return 0
    }

    override fun isImmediate(): Boolean {
        return false
    }

    override fun cycles(): List<Int> {
        return emptyList()
    }

    override fun bytes(): Int {
        return 0
    }

    override fun mnemonic(): String {
        return "EMPTY INSTRUCTION"
    }

    override fun operands(): List<JsonOperandMeta> {
        return emptyList()
    }

    override fun flags(): Map<String, String> {
        return emptyMap()
    }
}