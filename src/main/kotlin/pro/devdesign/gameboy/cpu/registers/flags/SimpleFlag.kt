package pro.devdesign.gameboy.cpu.registers.flags

import pro.devdesign.gameboy.cpu.registers.Register

class SimpleFlag : Flag {

    private val f: Register
    private val i: Int

    constructor(f: Register, i: Int) {
        this.f = f
        this.i = i
    }

    override fun enable() {
        f.set(f.get().or(1.shl(i)))
    }

    override fun disable() {
        f.set(f.get().and(1.shl(i).inv()))
    }

    override fun isEnabled(): Boolean {
        return f.get().and(1.shl(i)) != 0
    }

    override fun setEnabled(enabled: Boolean) {
        if (enabled) {
            enable()
        } else {
            disable()
        }
    }

    override fun toString(): String {
        return if (isEnabled()) {
            "1"
        } else {
            "0"
        }
    }
}