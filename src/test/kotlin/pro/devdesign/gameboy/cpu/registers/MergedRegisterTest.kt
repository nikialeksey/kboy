package pro.devdesign.gameboy.cpu.registers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MergedRegisterTest {
    @Test
    fun checkMergedRegisterGet() {
        Assertions
            .assertThat(
                MergedRegister(Register8(0xA1), Register8(0x23)).get()
            )
            .isEqualTo(0xA123)
    }

    @Test
    fun checkMergedRegisterSet() {
        val a = Register8(0xA1)
        val b = Register8(0x23)
        val ab = MergedRegister(a, b)
        ab.set(0x1234)
        Assertions
            .assertThat(
                a.get()
            )
            .isEqualTo(0x12)
        Assertions
            .assertThat(
                b.get()
            )
            .isEqualTo(0x34)
    }
}