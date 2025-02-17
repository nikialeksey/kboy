package com.alexeycode.kboy.gb.cpu.opcodes

interface InstructionMeta {
    fun opcode(): Int
    fun isImmediate(): Boolean
    fun cycles(): Cycles
    fun bytes(): Int
    fun mnemonic(): String
    fun operands(): List<OperandMeta>
    fun flags(): Map<String, String>

    class Dummy : InstructionMeta {
        override fun opcode(): Int {
            return 0
        }

        override fun isImmediate(): Boolean {
            return false
        }

        override fun cycles(): Cycles {
            return Cycles.Dummy()
        }

        override fun bytes(): Int {
            return 0
        }

        override fun mnemonic(): String {
            return ""
        }

        override fun operands(): List<OperandMeta> {
            return emptyList()
        }

        override fun flags(): Map<String, String> {
            return emptyMap()
        }

    }
}