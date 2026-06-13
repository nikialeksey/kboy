package com.alexeycode.kboy.gb.cpu.registers

import kotlin.test.Test
import kotlin.test.assertEquals

class MergedRegisterTest {
    @Test
    fun checkMergedRegisterGet() {
        assertEquals(
            0xA123,
            MergedRegister(Register8(0xA1), Register8(0x23)).get()
        )
    }

    @Test
    fun checkMergedRegisterSet() {
        val a = Register8(0xA1)
        val b = Register8(0x23)
        val ab = MergedRegister(a, b)
        ab.set(0x1234)

        assertEquals(
            0x12,
            a.get()
        )
        assertEquals(
            0x34,
            b.get()
        )
    }
}
