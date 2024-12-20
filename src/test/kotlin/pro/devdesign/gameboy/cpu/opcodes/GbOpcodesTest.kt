package pro.devdesign.gameboy.cpu.opcodes

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GbOpcodesTest {
    @Test
    fun checkInstructionRepresentation() {
        Assertions
            .assertThat(
                GbOpcodes()
                    .instructionMeta(0x08)
                    .toString()
            )
            .isEqualTo("LD       a16, SP")
    }

    @Test
    fun asd() {
        val opcodes = GbOpcodes()
        println(1.shl(7) == 0b1000_0000)
    }
}