package pro.devdesign.gameboy.cpu.instructions

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import java.lang.Integer.max

class PrintableInstructions(
    private val origin: Instructions
) : Instructions {

    private var depth = 0

    override fun instruction(address: Int): ReadInstruction {
        val result = origin.instruction(address)

        val (_, _, meta, operands) = result
        val addressPart = Ansi.colorize(
            address.toString(16).uppercase().padStart(4, padChar = '0'),
            Attribute.BRIGHT_GREEN_TEXT()
        )
        val instructionCode = Ansi.colorize(
            "0x${meta.opcode().toString(16).padStart(2, padChar = '0').uppercase()}",
            Attribute.BRIGHT_BLACK_TEXT()
        )
        val mnemonic = meta.mnemonic()
        val instructionName = Ansi.colorize(
            mnemonic.padEnd(8), Attribute.BOLD()
        )
        val instructionPart = "$instructionCode $instructionName"
        val operandsPart = Ansi.colorize(
            operands.joinToString { it.toString() },
            Attribute.BRIGHT_BLACK_TEXT(),
            Attribute.BOLD()
        )

        println("${"    ".repeat(depth)}$addressPart $instructionPart $operandsPart")

        if (mnemonic.contains("call", ignoreCase = true) || mnemonic.contains("rst", ignoreCase = true)) {
            depth++
        } else if (mnemonic.contains("ret", ignoreCase = true)) {
            depth = max(depth - 1, 0)
        }

        return result
    }

}