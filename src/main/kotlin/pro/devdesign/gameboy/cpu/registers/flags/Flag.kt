package pro.devdesign.gameboy.cpu.registers.flags

interface Flag {
    fun enable()
    fun disable()
    fun isEnabled(): Boolean
    fun setEnabled(enabled: Boolean)
}