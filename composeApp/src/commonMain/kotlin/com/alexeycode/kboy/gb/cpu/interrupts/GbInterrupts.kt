package com.alexeycode.kboy.gb.cpu.interrupts

class GbInterrupts(
    private var ime: Boolean = false,
    private var ieFlag: Int = 0x00,
    private var ifFlag: Int = 0xE1
) : Interrupts {

    private var imeEnableCountDown = 0

    override fun enable() {
        imeEnableCountDown = 2 // interrupt run delayed for one command execution
    }

    override fun disable() {
        imeEnableCountDown = 0
        ime = false
    }

    override fun ieFlag(): Int {
        return ieFlag
    }

    override fun updateIeFlag(flag: Int) {
        ieFlag = flag
    }

    override fun ifFlag(): Int {
        return ifFlag
    }

    override fun updateIfFlag(flag: Int) {
        ifFlag = flag.or(0xE0)
    }

    override fun requestTimer() {
        ifFlag = ifFlag.or(1.shl(2))
    }

    override fun tryRun(run: (address: Int) -> Unit) {
        if (imeEnableCountDown > 0) {
            imeEnableCountDown--
            if (imeEnableCountDown == 0) {
                ime = true
            }
        }

        if (ime) {
            tryExecuteInterrupt(run)
        }
    }

    private fun tryExecuteInterrupt(run: (address: Int) -> Unit) {
        if (isNeededToExec(0, ieFlag, ifFlag)) { // vBlank
            ifFlag = ifFlag.and(1.shl(0).inv())
            ime = false
            run(0x0040)
        } else if (isNeededToExec(1, ieFlag, ifFlag)) { // LCD
            ifFlag = ifFlag.and(1.shl(1).inv())
            ime = false
            run(0x0048)
        } else if (isNeededToExec(2, ieFlag, ifFlag)) { // Timer
            ifFlag = ifFlag.and(1.shl(2).inv())
            ime = false
            run(0x0050)
        } else if (isNeededToExec(3, ieFlag, ifFlag)) { // Serial
            ifFlag = ifFlag.and(1.shl(3).inv())
            ime = false
            run(0x0058)
        } else if (isNeededToExec(4, ieFlag, ifFlag)) { // Joypad
            ifFlag = ifFlag.and(1.shl(4).inv())
            ime = false
            run(0x0060)
        }
    }

    private fun isNeededToExec(
        interruptIndex: Int,
        ieFlag: Int,
        ifFlag: Int
    ): Boolean {
        val interruptBit = 1.shl(interruptIndex)
        val enabled = ieFlag.and(interruptBit) != 0
        val requested = ifFlag.and(interruptBit) != 0
        return enabled && requested
    }
}