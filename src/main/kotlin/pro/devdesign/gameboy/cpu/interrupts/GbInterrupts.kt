package pro.devdesign.gameboy.cpu.interrupts

/**
 * https://gbdev.io/pandocs/Timer_and_Divider_Registers.html
 * https://hacktix.github.io/GBEDG/timers/#-ff04---divider-register--div-
 * https://gbdev.io/pandocs/Timer_Obscure_Behaviour.html#relation-between-timer-and-divider-register
 */
class GbInterrupts(
    private var ime: Int = 0,
    private var ieFlag: Int = 0x00,
    private var ifFlag: Int = 0xE1
) : Interrupts {

    override fun enable() {
        ime = 2 // interrupt run delayed for one command execution
    }

    override fun disable() {
        ime = 0
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
        if (ime > 0) {
            ime--
            if (ime == 0) {
                executeInterrupts(run)
            }
        }
    }

    private fun executeInterrupts(run: (address: Int) -> Unit) {
        if (isNeededToExec(0, ieFlag, ifFlag)) { // vBlank
            ifFlag = ifFlag.and(1.shl(0).inv())
            run(0x0040)
        } else if (isNeededToExec(1, ieFlag, ifFlag)) { // LCD
            ifFlag = ifFlag.and(1.shl(1).inv())
            run(0x0048)
        } else if (isNeededToExec(2, ieFlag, ifFlag)) { // Timer
            ifFlag = ifFlag.and(1.shl(2).inv())
            run(0x0050)
        } else if (isNeededToExec(3, ieFlag, ifFlag)) { // Serial
            ifFlag = ifFlag.and(1.shl(3).inv())
            run(0x0058)
        } else if (isNeededToExec(4, ieFlag, ifFlag)) { // Joypad
            ifFlag = ifFlag.and(1.shl(4).inv())
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