package pro.devdesign.gameboy.cpu

import org.junit.jupiter.api.Test
import pro.devdesign.gameboy.cartridge.GbCartridge
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cpu.instructions.GbCartridgeInstructions
import pro.devdesign.gameboy.cpu.instructions.PrintableInstructions
import pro.devdesign.gameboy.cpu.interrupts.GbInterrupts
import pro.devdesign.gameboy.cpu.opcodes.GbOpcodes
import pro.devdesign.gameboy.cpu.registers.InMemoryRegisters
import pro.devdesign.gameboy.mem.GbMemory
import pro.devdesign.gameboy.mem.PrintableMemory
import pro.devdesign.gameboy.serial.BufferSerial

internal class GbCpuTest {
    @Test
    fun cpuInstrs() {
        val serial = BufferSerial()
        val ram = GbMemory(serial)
        val cartridge = GbCartridge(GbCartridgeData(javaClass, "/06-ld r,r.gb"))
        cartridge.upload(ram)

        val registers = InMemoryRegisters()
        val memory = PrintableMemory(ram)
        val instructions = GbCartridgeInstructions(GbOpcodes(), ram)
        val cpu = GbCpu(
            registers = registers,
            memory = ram, // memory,
            interrupts = GbInterrupts(registers, memory),
            instructions = instructions // PrintableInstructions(instructions, registers)
        )

        cpu.executeNext(80000)

        println(String(serial.asByteArray()))
    }
}