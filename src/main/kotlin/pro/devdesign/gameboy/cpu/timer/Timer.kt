package pro.devdesign.gameboy.cpu.timer

interface Timer {
    fun tick(clockCycles: Int)

    fun div(): Int
    fun resetDiv()

    fun tima(): Int
    fun updateTima(tima: Int)

    fun tma(): Int
    fun updateTma(tma: Int)

    fun tac(): Int
    fun updateTac(tac: Int)

}