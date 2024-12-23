package pro.devdesign.gameboy.cpu

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import pro.devdesign.gameboy.cartridge.GbCartridge
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cpu.instructions.DoctorPrintableInstructions
import pro.devdesign.gameboy.cpu.instructions.GbCartridgeInstructions
import pro.devdesign.gameboy.cpu.instructions.PrintableInstructions
import pro.devdesign.gameboy.cpu.interrupts.GbInterrupts
import pro.devdesign.gameboy.cpu.opcodes.GbOpcodes
import pro.devdesign.gameboy.cpu.registers.InMemoryRegisters
import pro.devdesign.gameboy.mem.GbMemory
import pro.devdesign.gameboy.mem.PrintableMemory
import pro.devdesign.gameboy.serial.BufferSerial

internal class GbCpuTest {
    @ParameterizedTest
    @ValueSource(strings = [
        "/cpu-instrs/01-special.gb",
        "/cpu-instrs/02-interrupts.gb",
        "/cpu-instrs/03-op sp,hl.gb",
        "/cpu-instrs/04-op r,imm.gb",
        "/cpu-instrs/05-op rp.gb",
        "/cpu-instrs/06-ld r,r.gb",
        "/cpu-instrs/07-jr,jp,call,ret,rst.gb",
        "/cpu-instrs/08-misc instrs.gb",
        "/cpu-instrs/09-op r,r.gb",
        "/cpu-instrs/10-bit ops.gb",
        "/cpu-instrs/11-op a,(hl).gb",
    ])
    fun cpuInstrsIndividual(gbFileName: String) {
        val serial = BufferSerial()
        val ram = GbMemory(serial)
        val cartridge = GbCartridge(GbCartridgeData(javaClass, gbFileName))
        cartridge.upload(ram)

        val registers = InMemoryRegisters()
        val instructions = GbCartridgeInstructions(GbOpcodes(), ram)
        val cpu = GbCpu(
            registers = registers,
            memory = ram,
            interrupts = GbInterrupts(registers, ram),
            instructions = instructions
        )
        cpu.executeNext(2827700)

        val outputMessage = String(serial.asByteArray())
        Assertions.assertTrue(
            outputMessage.contains("Passed"),
            "Actual output message: $outputMessage"
        )
    }

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
            instructions = PrintableInstructions(instructions, registers)
        )

//        cpu.executeNext(25000)
        cpu.executeNext(78277)
//        cpu.executeNext(800277)

        println("Output:")
        println(String(serial.asByteArray()))
    }
}