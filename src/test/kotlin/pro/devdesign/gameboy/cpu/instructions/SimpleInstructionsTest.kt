package pro.devdesign.gameboy.cpu.instructions

import org.junit.jupiter.api.Test
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cpu.opcodes.GbOpcodes

internal class SimpleInstructionsTest {
    @Test
    fun asd() {
        val instructions = PrintableInstructions(
            GbCartridgeInstructions(
                GbOpcodes(),
                GbCartridgeData(
                    javaClass,
                    "/cpu_instrs.gb"
                )
            )
        )
        var address = 0x430
        for (i in 0 until 30) {
            val (nextAddress, _, _, _) = instructions.instruction(address)
            address = nextAddress
        }
    }

    @Test
    fun helloWorldInstructions() {
        val instructions = PrintableInstructions(
            GbCartridgeInstructions(
                GbOpcodes(),
                GbCartridgeData(
                    javaClass,
                    "/hello_world.gb"
                )
            )
        )
        var address = 0x00
        for (i in 0 until 50) {
            val (nextAddress, _, _, _) = instructions.instruction(address)
            address = nextAddress
        }
    }
}