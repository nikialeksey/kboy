package com.alexeycode.kboy.gb.cpu.opcodes

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class JsonOperandMeta(
    private val operandJson: JsonObject
) : OperandMeta {


    private val _isImmediate by lazy(mode = LazyThreadSafetyMode.NONE) {
        operandJson["immediate"]?.jsonPrimitive?.content?.toBoolean() ?: false
    }
    private val _name by lazy(mode = LazyThreadSafetyMode.NONE) {
        operandJson["name"]?.jsonPrimitive?.content ?: "Unknown operand name"
    }
    private val _bytes by lazy(mode = LazyThreadSafetyMode.NONE) {
        operandJson["bytes"]?.jsonPrimitive?.int ?: 0
    }
    private val _sign by lazy(mode = LazyThreadSafetyMode.NONE) {
        val increment = operandJson["increment"]
            ?.jsonPrimitive
            ?.content
            ?.toBoolean()
        val decrement = operandJson["decrement"]
            ?.jsonPrimitive
            ?.content
            ?.toBoolean()
        if (increment == true) {
            1
        } else if (decrement == true) {
            -1
        } else {
            0
        }
    }

    override fun isImmediate(): Boolean {
        return _isImmediate
    }

    override fun name(): String {
        return _name
    }

    override fun bytes(): Int {
        return _bytes
    }

    override fun sign(): Int {
        return _sign
    }

    override fun toString(): String {
        return name()
    }
}
