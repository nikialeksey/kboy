package pro.devdesign.gameboy.cpu.registers.flags

class SimpleFlag : Flag {

    private var value: Boolean

    constructor() : this(false)

    constructor(value: Boolean) {
        this.value = value
    }

    override fun enable() {
        value = true
    }

    override fun disable() {
        value = false
    }

    override fun isEnabled(): Boolean {
        return value
    }

    override fun setEnabled(enabled: Boolean) {
        value = enabled
    }
}