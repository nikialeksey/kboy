package com.alexeycode.kboy.cpu.opcodes

import kotlinx.serialization.json.*

class JsonInstructionMeta(
    private val opcode: Int,
    private val instructionJson: JsonObject
) : InstructionMeta {

    private val _isImmediate by lazy {
        instructionJson["immediate"]?.jsonPrimitive?.content?.toBoolean() ?: false
    }
    private val _cycles by lazy {
        val cycles = instructionJson["cycles"]?.jsonArray?.map { it.jsonPrimitive.int } ?: emptyList()

        if (cycles.isEmpty()) {
            GbCycles(1, 1)
        } else if (cycles.size == 1) {
            GbCycles(cycles[0], cycles[0])
        } else {
            GbCycles(cycles[0], cycles[1])
        }
    }
    private val _bytes by lazy {
        instructionJson["bytes"]?.jsonPrimitive?.int ?: 0
    }
    private val _mnemonic by lazy {
        instructionJson["mnemonic"]?.jsonPrimitive?.content ?: "Unknown mnemonic"
    }
    private val _operands by lazy {
        instructionJson["operands"]
            ?.jsonArray
            ?.map {
                JsonOperandMeta(it.jsonObject)
            }
            ?: emptyList()
    }
    private val _flags by lazy {
        instructionJson["flags"]
            ?.jsonObject
            ?.toMap()
            ?.mapValues { it.value.jsonPrimitive.content }
            ?: emptyMap()
    }

    override fun opcode(): Int {
        return opcode
    }

    override fun isImmediate(): Boolean {
        return _isImmediate
    }

    override fun cycles(): Cycles {
        return _cycles
    }

    override fun bytes(): Int {
        return _bytes
    }

    override fun mnemonic(): String {
        return _mnemonic
    }

    override fun operands(): List<OperandMeta> {
        return _operands
    }

    override fun flags(): Map<String, String> {
        return _flags
    }

    override fun toString(): String {
        return "${mnemonic().padEnd(8)} ${operands().joinToString { it.toString() }}"
    }
}
