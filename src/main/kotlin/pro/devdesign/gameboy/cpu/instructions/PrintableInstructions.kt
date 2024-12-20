package pro.devdesign.gameboy.cpu.instructions

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import pro.devdesign.gameboy.cpu.instructions.address.SimpleAddress
import pro.devdesign.gameboy.cpu.registers.Registers

class PrintableInstructions(
    private val origin: Instructions,
    private val registers: Registers
) : Instructions {

    override fun instruction(address: Int): ReadInstruction {
        val result = origin.instruction(address)

        val (_, _, meta, operands) = result
        val addressPart = Ansi.colorize(
            SimpleAddress(address).toString(),
            Attribute.BRIGHT_GREEN_TEXT()
        )
        val instructionCode = Ansi.colorize(
            "0x${meta.opcode().toString(16).uppercase().padStart(2, padChar = '0')}",
            Attribute.BRIGHT_BLACK_TEXT()
        )
        val mnemonic = meta.mnemonic()
        val instructionName = Ansi.colorize(
            mnemonic.padEnd(8),
            Attribute.BOLD()
        )
        val instructionPart = "$instructionCode $instructionName"
        val operandsPart = Ansi.colorize(
            operands.joinToString { it.toString() }.padEnd(16),
            Attribute.BRIGHT_BLACK_TEXT(),
            Attribute.BOLD()
        )

        val registerPart = Ansi.colorize(
            "Registers: ",
            Attribute.BRIGHT_BLACK_TEXT()
        ) + Ansi.colorize(
            "A: 0x${registers.a()}, " +
                    "BC: 0x${registers.bc()}," +
                    "DE: 0x${registers.de()}, " +
                    "HL: 0x${registers.hl()}, " +
                    "SP: 0x${registers.sp()}, " +
                    "PC: 0x${registers.pc()}",
            Attribute.BRIGHT_MAGENTA_TEXT()
        )

        val flag = registers.flag()
        val flagsPart = Ansi.colorize(
            "Flags: ",
            Attribute.BRIGHT_BLACK_TEXT()
        ) + Ansi.colorize(
            flag.toString(),
            Attribute.BRIGHT_CYAN_TEXT()
        )

        println("$addressPart $instructionPart $operandsPart $registerPart, $flagsPart")

        return result
    }

}