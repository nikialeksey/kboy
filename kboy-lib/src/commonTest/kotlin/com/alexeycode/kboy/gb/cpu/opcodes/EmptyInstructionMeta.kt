package com.alexeycode.kboy.gb.cpu.opcodes

class EmptyInstructionMeta : InstructionMeta {

    private val emptyCycles = GbCycles(0, 0)

    override fun opcode(): Int {
        return 0
    }

    override fun isImmediate(): Boolean {
        return false
    }

    override fun cycles(): Cycles {
        return emptyCycles
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