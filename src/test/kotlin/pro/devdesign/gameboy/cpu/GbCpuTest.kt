package pro.devdesign.gameboy.cpu

import org.junit.jupiter.api.Test
import pro.devdesign.gameboy.cartridge.GbCartridge
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cpu.instructions.GbCartridgeInstructions
import pro.devdesign.gameboy.cpu.instructions.PrintableInstructions
import pro.devdesign.gameboy.cpu.opcodes.GbOpcodes
import pro.devdesign.gameboy.cpu.registers.InMemoryRegisters
import pro.devdesign.gameboy.mem.InMemoryMemory

internal class GbCpuTest {
    @Test
    fun asd() {
        val cpu = GbCpu(
            registers = InMemoryRegisters(),
            memory = InMemoryMemory(),
            instructions = //PrintableInstructions(
                GbCartridgeInstructions(
                    GbOpcodes(),
                    GbCartridgeData(javaClass, "/cpu_instrs.gb")
                )
            //)
        )

        cpu.executeNext()
    }

    @Test
    fun helloWorld() {
        val ram = InMemoryMemory()
        val cartridge = GbCartridge(GbCartridgeData(javaClass, "/hello_world.gb"))
        cartridge.upload(ram)
        val registers = InMemoryRegisters()
        val cpu = GbCpu(
            registers = registers,
            memory = ram,
            instructions = PrintableInstructions(
                GbCartridgeInstructions(GbOpcodes(), ram)
            )
        )

        cpu.executeNext(100)
    }
}