package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.cpu.instructions.ExtInstruction
import com.alexeycode.kboy.gb.cpu.instructions.Instruction
import com.alexeycode.kboy.gb.cpu.instructions.SimpleInstruction
import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.cpu.registers.Registers
import com.alexeycode.kboy.gb.mem.Memory
import com.alexeycode.kboy.gb.mem.readNext8

class GbCpu(
    private val r: Registers,
    private val mem: Memory,
    private val interrupts: Interrupts,
    private val instruction: Instruction = SimpleInstruction(r, mem, interrupts),
    private val extInstruction: Instruction = ExtInstruction(r, mem)
) : Cpu {

    private var haltMode = false

    override fun tick(): Int {
        return if (haltMode) {
            if (interrupts.ieFlag().and(interrupts.ifFlag()) != 0) {
                haltMode = false
            }

            4
        } else {
            val clockCyclesSpentOnInterrupts = runInterrupts()

            if (clockCyclesSpentOnInterrupts == 0) {
                val oldPc = r.pc().get()
                val code = mem.readNext8(r)
                val isExt = code == 0xCB
                val opcode = if (isExt) {
                    mem.readNext8(r)
                } else {
                    code
                }

                val clockCyclesSpent = try {
                    if (isExt) {
                        extInstruction.execute(opcode)
                    } else {
                        instruction.execute(opcode)
                    }
                } catch (e: IllegalArgumentException) {
                    throw RuntimeException("CPU can not execute instruction at address 0x${oldPc.toString(16).uppercase()}", e)
                }

                if (!isExt && opcode == 0x76) {
                    haltMode = true
                }

                clockCyclesSpent
            } else {
                clockCyclesSpentOnInterrupts
            }
        }
    }

    private fun runInterrupts(): Int {
        var clockCyclesSpent = 0
        val address = interrupts.tryRun()
        if (address != 0) {
            val pc = r.pc().get()
            mem.write8(r.sp().get() - 1, pc.shr(8).and(0xFF))
            mem.write8(r.sp().get() - 2, pc.and(0xFF))
            r.sp().set(r.sp().get() - 2)
            r.pc().set(address)

            // TODO should I do timer.tick() here?
            clockCyclesSpent = 12 // maybe 12, because write+write+execute
        }
        return clockCyclesSpent
    }
}