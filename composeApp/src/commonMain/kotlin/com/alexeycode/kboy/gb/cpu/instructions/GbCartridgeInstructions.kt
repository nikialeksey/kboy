package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.instructions.operands.Operand
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80RegisterOperand
import com.alexeycode.kboy.gb.cpu.instructions.operands.Z80ValueOperand
import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.opcodes.Opcodes
import com.alexeycode.kboy.gb.mem.Rom

class GbCartridgeInstructions : Instructions {

    private val opcodes: Opcodes
    private val data: Rom

    private var nextAddress: Int = 0
    private var isExtInstruction: Boolean = false
    private var instructionMeta: InstructionMeta = InstructionMeta.Dummy()
    private var operands: List<Operand> = emptyList()

    private val dummyOperand = Operand.Dummy()

    constructor(opcodes: Opcodes, data: Rom) {
        this.opcodes = opcodes
        this.data = data
    }

    override fun loadInstruction(address: Int) {
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
                var index = 0
                for (a in p until (p + bytes)) {
                    val part = data.read8(a)
                    value += part * (1 shl (index * 8))
                    index++
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
                val isFlagOperand = operandName[0] == 'N' || operandName[0] == 'Z' // "NZ", "Z", "NC"
                val mnemonic = instructionMeta.mnemonic()
                val isCFlagOperand = operandName[0] == 'C' && (mnemonic == "CALL" || mnemonic == "RET" || mnemonic == "JR" || mnemonic == "JP")
                if (isFlagOperand || isCFlagOperand) {
                    dummyOperand
                } else if ((operandName.length == 3 && operandName.last() == 'H') || (operandName.length == 1 && operandName[0].isDigit())) {
                    dummyOperand
                } else {
                    Z80RegisterOperand(
                        operandName,
                        operandMeta.sign(),
                        operandMeta.isImmediate()
                    )
                }
            }
        }

        this.nextAddress = p
        this.isExtInstruction = isExtInstruction
        this.instructionMeta = instructionMeta
        this.operands = operands
    }

    override fun nextAddress(): Int {
        return this.nextAddress
    }

    override fun isExtInstruction(): Boolean {
        return this.isExtInstruction
    }

    override fun instructionMeta(): InstructionMeta {
        return this.instructionMeta
    }

    override fun operands(): List<Operand> {
        return this.operands
    }
}