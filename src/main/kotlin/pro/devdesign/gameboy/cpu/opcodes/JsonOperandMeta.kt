package pro.devdesign.gameboy.cpu.opcodes

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import org.cactoos.Scalar
import org.cactoos.scalar.Sticky

class JsonOperandMeta : OperandMeta {

    private val isImmediate: Scalar<Boolean>
    private val name: Scalar<String>
    private val bytes: Scalar<Int>
    private val sign: Scalar<Int>

    constructor(operandJson: JsonObject) : this(
        Sticky {
            operandJson["immediate"]?.jsonPrimitive?.content?.toBoolean() ?: false
        },
        Sticky {
            operandJson["name"]?.jsonPrimitive?.content ?: "Unknown operand name"
        },
        Sticky {
            operandJson["bytes"]?.jsonPrimitive?.int ?: 0
        },
        Sticky {
            val increment = operandJson["increment"]
                ?.jsonPrimitive
                ?.content
                ?.toBoolean()
            val decrement = operandJson["decrement"]
                ?.jsonPrimitive
                ?.content
                ?.toBoolean()
            return@Sticky if (increment == true) {
                1
            } else if (decrement == true) {
                -1
            } else {
                0
            }
        }
    )

    constructor(
        isImmediate: Scalar<Boolean>,
        name: Scalar<String>,
        bytes: Scalar<Int>,
        sign: Scalar<Int>
    ) {
        this.isImmediate = isImmediate
        this.name = name
        this.bytes = bytes
        this.sign = sign
    }

    override fun isImmediate(): Boolean {
        return isImmediate.value()
    }

    override fun name(): String {
        return name.value()
    }

    override fun bytes(): Int {
        return bytes.value()
    }

    override fun sign(): Int {
        return sign.value()
    }

    override fun toString(): String {
        return name()
    }
}
