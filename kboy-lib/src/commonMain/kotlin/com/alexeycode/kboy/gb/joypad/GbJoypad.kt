package com.alexeycode.kboy.gb.joypad

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts

class GbJoypad(
    private val interrupts: Interrupts,
    private var value: Int = 0xCF,
    private val right: Button = GbButton(interrupts, 0x10, 0x01),
    private val left: Button = GbButton(interrupts, 0x10, 0x02),
    private val up: Button = GbButton(interrupts, 0x10, 0x04),
    private val down: Button = GbButton(interrupts, 0x10, 0x08),
    private val a: Button = GbButton(interrupts, 0x20, 0x01),
    private val b: Button = GbButton(interrupts, 0x20, 0x02),
    private val select: Button = GbButton(interrupts, 0x20, 0x04),
    private val start: Button = GbButton(interrupts, 0x20, 0x08)
) : Joypad {

    private val buttons = listOf(right, left, up, down, a, b, select, start)

    override fun get(): Int {
        var result = value.or(0b1100_1111)

        for (button in buttons) {
            if (button.line().and(value) == 0) {
                result = result.and(button.mask().inv().and(0xFF))
            }
        }

        return result
    }

    override fun update(value: Int) {
        this.value = value.and(0b0011_0000)
    }

    override fun right(): Button {
        return right
    }

    override fun left(): Button {
        return left
    }

    override fun up(): Button {
        return up
    }

    override fun down(): Button {
        return down
    }

    override fun a(): Button {
        return a
    }

    override fun b(): Button {
        return b
    }

    override fun select(): Button {
        return select
    }

    override fun start(): Button {
        return start
    }


}