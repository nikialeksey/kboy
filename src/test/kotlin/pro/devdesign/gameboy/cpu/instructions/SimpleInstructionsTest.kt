package pro.devdesign.gameboy.cpu.instructions

import org.junit.jupiter.api.Test
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cpu.opcodes.GbOpcodes
import pro.devdesign.gameboy.cpu.registers.InMemoryRegisters

internal class SimpleInstructionsTest {
    @Test
    fun asd() {
        val instructions = PrintableInstructions(
            GbCartridgeInstructions(
                GbOpcodes(),
                GbCartridgeData(
                    javaClass,
                    "/mooneye_serial_test.gb"
                )
            ),
            InMemoryRegisters()
        )
        var address = 0x150
        for (i in 0 until 16000) {
            val (nextAddress, _, _, _) = instructions.instruction(address)
            address = nextAddress.asInt()
        }
    }
}