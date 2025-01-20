package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.address.SimpleAddress
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80FlagOperand
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80LiteralOperand
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80RegisterOperand
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80ValueOperand
import com.alexeycode.kboy.gb.cpu.opcodes.Opcodes
import com.alexeycode.kboy.gb.mem.Rom

class GbCartridgeInstructions : Instructions {

    private val opcodes: Opcodes
    private val data: Rom

    constructor(opcodes: Opcodes, data: Rom) {
        this.opcodes = opcodes
        this.data = data
    }

    override fun instruction(address: Int): ReadInstruction {
        var p = address
        val code = data.read8(p++)
        val isExtInstruction = code == 0xCB
        val instructionMeta = if (isExtInstruction) {
            opcodes.cbInstructionMeta(data.read8(p++))
        } else {
            opcodes.instructionMeta(code)
        }

        val operands = instructionMeta.operands().map { operandMeta ->
            val bytes = operandMeta.bytes()
            val operandName = operandMeta.name()
            if (bytes != 0) {
                var value = 0
                (p until (p + bytes)).map { data.read8(it) }.forEachIndexed { index, part ->
                    value += part * (1 shl (index * 8))
                }
                p += bytes
                if (operandName == "r8") {
                    // convert to signed
                    if (value.and(0b1000_0000) != 0) {
                        value -= 0b1_0000_0000
                    }
                }
                Z80ValueOperand(value, operandMeta.bytes(), operandMeta.isImmediate())
            } else {
                if (operandName in arrayOf("NZ", "Z", "NC") || (operandName == "C" && instructionMeta.mnemonic() in arrayOf("CALL", "RET", "JR", "JP"))) {
                    Z80FlagOperand(operandName, operandMeta.isImmediate())
                } else if (operandName.any(Char::isDigit)) {
                    Z80LiteralOperand(operandName)
                } else {
                    Z80RegisterOperand(
                        operandName,
                        operandMeta.sign(),
                        operandMeta.isImmediate()
                    )
                }
            }
        }

        return ReadInstruction(SimpleAddress(p), isExtInstruction, instructionMeta, operands)
    }
}