package com.alexeycode.kboy.gb.cpu.instructions

import com.alexeycode.kboy.gb.cpu.opcodes.InstructionMeta
import com.alexeycode.kboy.gb.cpu.opcodes.Opcodes
import com.alexeycode.kboy.gb.mem.Rom

class GbCartridgeInstructions : Instructions {

    private val opcodes: Opcodes
    private val data: Rom

    private var opcode: Int = 0
    private var nextAddress: Int = 0
    private var isExtInstruction: Boolean = false
    private var instructionMeta: InstructionMeta = InstructionMeta.Dummy()

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

        this.nextAddress = p
        this.isExtInstruction = isExtInstruction
        this.instructionMeta = instructionMeta
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
}