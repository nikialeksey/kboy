package pro.devdesign.gameboy.cpu.opcodes

import kotlinx.serialization.json.*
import org.cactoos.Scalar
import org.cactoos.scalar.Sticky

class JsonInstructionMeta : InstructionMeta {

    private val opcode: Int
    private val isImmediate: Scalar<Boolean>
    private val cycles: Scalar<List<Int>>
    private val bytes: Scalar<Int>
    private val mnemonic: Scalar<String>
    private val operands: Scalar<List<OperandMeta>>
    private val flags: Scalar<Map<String, String>>

    constructor(opcode: Int, instructionJson: JsonObject) : this(
        opcode,
        Sticky {
            instructionJson["immediate"]?.jsonPrimitive?.content?.toBoolean() ?: false
        },
        Sticky {
            instructionJson["cycles"]?.jsonArray?.map { it.jsonPrimitive.int } ?: emptyList()
        },
        Sticky {
            instructionJson["bytes"]?.jsonPrimitive?.int ?: 0
        },
        Sticky {
            instructionJson["mnemonic"]?.jsonPrimitive?.content ?: "Unknown mnemonic"
        },
        Sticky {
            instructionJson["operands"]
                ?.jsonArray
                ?.map {
                    JsonOperandMeta(it.jsonObject)
                }
                ?: emptyList()
        },
        Sticky {
            instructionJson["flags"]
                ?.jsonObject
                ?.toMap()
                ?.mapValues { it.value.jsonPrimitive.content }
                ?: emptyMap()
        }
    )

    constructor(
        opcode: Int,
        isImmediate: Scalar<Boolean>,
        cycles: Scalar<List<Int>>,
        bytes: Scalar<Int>,
        mnemonic: Scalar<String>,
        operands: Scalar<List<OperandMeta>>,
        flags: Scalar<Map<String, String>>
    ) {
        this.opcode = opcode
        this.isImmediate = isImmediate
        this.cycles = cycles
        this.bytes = bytes
        this.mnemonic = mnemonic
        this.operands = operands
        this.flags = flags
    }

    override fun opcode(): Int {
        return opcode
    }

    override fun isImmediate(): Boolean {
        return isImmediate.value()
    }

    override fun cycles(): List<Int> {
        return cycles.value()
    }

    override fun bytes(): Int {
        return bytes.value()
    }

    override fun mnemonic(): String {
        return mnemonic.value()
    }

    override fun operands(): List<OperandMeta> {
        return operands.value()
    }

    override fun flags(): Map<String, String> {
        return flags.value()
    }

    override fun toString(): String {
        return "${mnemonic().padEnd(8)} ${operands().joinToString { it.toString() }}"
    }
}
