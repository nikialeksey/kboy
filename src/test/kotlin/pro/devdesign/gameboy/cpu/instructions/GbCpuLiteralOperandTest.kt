package pro.devdesign.gameboy.cpu.instructions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import pro.devdesign.gameboy.cpu.instructions.operands.Z80LiteralOperand
import pro.devdesign.gameboy.cpu.registers.InMemoryRegisters
import pro.devdesign.gameboy.mem.GbMemory
import pro.devdesign.gameboy.serial.BufferSerial

class GbCpuLiteralOperandTest {
    @Test
    fun checkRead16() {
        Assertions
            .assertThat(
                Z80LiteralOperand("38H").read16(GbMemory(BufferSerial()), InMemoryRegisters())
            )
            .isEqualTo(0x38)
    }
}