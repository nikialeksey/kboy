package pro.devdesign.gameboy.cpu.interrupts

interface Interrupts {
    fun enable()
    fun disable()

    fun ieFlag(): Int
    fun updateIeFlag(flag: Int)

    fun ifFlag(): Int
    fun updateIfFlag(flag: Int)

    fun requestTimer()

    fun tryRun(run: (address: Int) -> Unit)
}