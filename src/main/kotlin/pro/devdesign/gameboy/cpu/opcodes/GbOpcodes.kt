package pro.devdesign.gameboy.cpu.opcodes

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.cactoos.Scalar
import org.cactoos.scalar.Sticky
import java.io.InputStream

/**
 * https://gbdev.io/gb-opcodes/optables/
 * https://meganesu.github.io/generate-gb-opcodes/
 */
class GbOpcodes : Opcodes {

    private val codeToInstruction: Scalar<Array<InstructionMeta>>
    private val codeToPrefixedInstruction: Scalar<Array<InstructionMeta>>

    constructor() : this(GbOpcodes::class.java.getResourceAsStream("/Opcodes.json")!!)

    constructor(opcodesStream: InputStream) : this(
        Sticky {
            val opcodesString = opcodesStream.readAllBytes().decodeToString()
            Json.parseToJsonElement(opcodesString)
        }
    )

    constructor(opcodesJson: Scalar<JsonElement>) : this(
        Sticky {
            val instructions = Array<InstructionMeta>(256) { EmptyInstructionMeta() }
            val unprefixedOpcodes = opcodesJson.value().jsonObject["unprefixed"]
            unprefixedOpcodes!!.jsonObject
                .mapKeys { (code, _) ->
                    code.removePrefix("0x").toInt(16)
                }
                .forEach { code, instructionJson ->
                    instructions[code] = JsonInstructionMeta(code, instructionJson.jsonObject)
                }
            instructions
        },
        Sticky {
            val instructions = Array<InstructionMeta>(256) { EmptyInstructionMeta() }
            val cbprefixedOpcodes = opcodesJson.value().jsonObject["cbprefixed"]
            cbprefixedOpcodes!!.jsonObject
                .mapKeys { (code, _) ->
                    code.removePrefix("0x").toInt(16)
                }
                .forEach { code, instructionJson ->
                    instructions[code] = JsonInstructionMeta(code, instructionJson.jsonObject)
                }
            instructions
        }
    )

    constructor(
        codeToInstruction: Scalar<Array<InstructionMeta>>,
        codeToPrefixedInstruction: Scalar<Array<InstructionMeta>>
    ) {
        this.codeToInstruction = codeToInstruction
        this.codeToPrefixedInstruction = codeToPrefixedInstruction
    }

    override fun instructionMeta(code: Int): InstructionMeta {
        return codeToInstruction.value()[code]
    }

    override fun cbInstructionMeta(code: Int): InstructionMeta {
        return codeToPrefixedInstruction.value()[code]
    }
}