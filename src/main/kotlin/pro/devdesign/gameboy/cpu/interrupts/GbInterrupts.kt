package pro.devdesign.gameboy.cpu.interrupts

import pro.devdesign.gameboy.cpu.registers.Registers
import pro.devdesign.gameboy.mem.Memory

class GbInterrupts(
    private val registers: Registers,
    private val memory: Memory,
    private var ime: Boolean = false
) : Interrupts {

    override fun enable() {
        ime = true
        tryExecuteInterrupts()
    }

    override fun disable() {
        ime = false
    }

    private fun tryExecuteInterrupts() {
        if (ime) {
            val ieFlag = memory.read8(0xFFFF)
            val ifFlag = memory.read8(0xFF0F)

            tryExecuteInterrupt(0, 0x0040, ieFlag, ifFlag) // vBlank
            tryExecuteInterrupt(1, 0x0048, ieFlag, ifFlag) // LCD
            tryExecuteInterrupt(2, 0x0050, ieFlag, ifFlag) // Timer
            tryExecuteInterrupt(3, 0x0058, ieFlag, ifFlag) // Serial
            tryExecuteInterrupt(4, 0x0060, ieFlag, ifFlag) // Joypad
        }
    }

    private fun tryExecuteInterrupt(
        interruptIndex: Int,
        interruptAddress: Int,
        ieFlag: Int,
        ifFlag: Int
    ) {
        val interruptBit = 1.shl(interruptIndex)
        val enabled = ieFlag.and(interruptBit) != 0
        val requested = ifFlag.and(interruptBit) != 0
        if (enabled && requested) {
            val pc = registers.pc().get()
            memory.write8(registers.sp().get() - 1, pc.shr(8).and(0xFF))
            memory.write8(registers.sp().get() - 2, pc.and(0xFF))
            registers.sp().set(registers.sp().get() - 2)
            registers.pc().set(interruptAddress)
        }
    }
}