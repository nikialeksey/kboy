package com.alexeycode.kboy.cpu.opcodes

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

/**
 * https://gbdev.io/gb-opcodes/optables/
 * https://meganesu.github.io/generate-gb-opcodes/
 */
class GbOpcodes(
    private val opcodesBytes: ByteArray
) : Opcodes {

    private val opcodesJson by lazy(mode = LazyThreadSafetyMode.NONE) {
        val opcodesString = opcodesBytes.decodeToString()
        Json.parseToJsonElement(opcodesString)
    }
    private val codeToInstruction: Array<InstructionMeta> by lazy(mode = LazyThreadSafetyMode.NONE) {
        val instructions = Array<InstructionMeta>(256) { EmptyInstructionMeta() }
        val unprefixedOpcodes = opcodesJson.jsonObject["unprefixed"]
        unprefixedOpcodes!!.jsonObject
            .mapKeys { (code, _) ->
                code.removePrefix("0x").toInt(16)
            }
            .forEach { code, instructionJson ->
                instructions[code] = JsonInstructionMeta(code, instructionJson.jsonObject)
            }
        instructions
    }
    private val codeToPrefixedInstruction: Array<InstructionMeta> by lazy(mode = LazyThreadSafetyMode.NONE) {
        val instructions = Array<InstructionMeta>(256) { EmptyInstructionMeta() }
        val cbprefixedOpcodes = opcodesJson.jsonObject["cbprefixed"]
        cbprefixedOpcodes!!.jsonObject
            .mapKeys { (code, _) ->
                code.removePrefix("0x").toInt(16)
            }
            .forEach { code, instructionJson ->
                instructions[code] = JsonInstructionMeta(code, instructionJson.jsonObject)
            }
        instructions
    }

    override fun instructionMeta(code: Int): InstructionMeta {
        return codeToInstruction[code]
    }

    override fun cbInstructionMeta(code: Int): InstructionMeta {
        return codeToPrefixedInstruction[code]
    }
}