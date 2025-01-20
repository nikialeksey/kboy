package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.SimpleGb
import com.alexeycode.kboy.gb.cartridge.GbCartridge
import com.alexeycode.kboy.gb.cartridge.GbCartridgeData
import com.alexeycode.kboy.gb.cpu.instructions.GbCartridgeInstructions
import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.opcodes.GbOpcodes
import com.alexeycode.kboy.gb.cpu.registers.InMemoryRegisters
import com.alexeycode.kboy.gb.cpu.timer.GbTimer
import com.alexeycode.kboy.gb.mem.GbMemory
import com.alexeycode.kboy.gb.ppu.GbPpu
import com.alexeycode.kboy.gb.serial.BufferSerial
import kboy.composeapp.generated.resources.Res
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.test.Test
import kotlin.test.assertContains

@OptIn(ExperimentalResourceApi::class)
class GbCpuTest {

    @Test
    fun test01() = runTest {
        testCpuInstrsIndividual("cpu-instrs/01-special.gb")
    }

    @Test
    fun test02() = runTest {
        testCpuInstrsIndividual("cpu-instrs/02-interrupts.gb")
    }

    @Test
    fun test03() = runTest {
        testCpuInstrsIndividual("cpu-instrs/03-op sp,hl.gb")
    }

    @Test
    fun test04() = runTest {
        testCpuInstrsIndividual("cpu-instrs/04-op r,imm.gb")
    }

    @Test
    fun test05() = runTest {
        testCpuInstrsIndividual("cpu-instrs/05-op rp.gb")
    }

    @Test
    fun test06() = runTest {
        testCpuInstrsIndividual("cpu-instrs/06-ld r,r.gb")
    }

    @Test
    fun test07() = runTest {
        testCpuInstrsIndividual("cpu-instrs/07-jr,jp,call,ret,rst.gb")
    }

    @Test
    fun test08() = runTest {
        testCpuInstrsIndividual("cpu-instrs/08-misc instrs.gb")
    }

    @Test
    fun test09() = runTest {
        testCpuInstrsIndividual("cpu-instrs/09-op r,r.gb")
    }

    @Test
    fun test10() = runTest {
        testCpuInstrsIndividual("cpu-instrs/10-bit ops.gb")
    }

    @Test
    fun test11() = runTest {
        testCpuInstrsIndividual("cpu-instrs/11-op a,(hl).gb")
    }

    private suspend fun testCpuInstrsIndividual(gbFileName: String) {
        val interrupts = GbInterrupts()
        val timer = GbTimer(interrupts)
        val serial = BufferSerial()
        val ram = GbMemory(interrupts, timer, serial)
        val cartridge = GbCartridge(
            GbCartridgeData(
                Res.readBytes("files/$gbFileName")
            )
        )
        cartridge.upload(ram)

        val registers = InMemoryRegisters()
        val instructions = GbCartridgeInstructions(
            GbOpcodes(
                Res.readBytes("files/Opcodes.json")
            ),
            ram
        )
        val cpu = GbCpu(
            registers = registers,
            memory = ram,
            interrupts = interrupts,
            instructions = instructions,
        )
        val gb = SimpleGb(
            timer = timer,
            cpu = cpu,
            ppu = GbPpu(memory = ram)
        )
        while (true) {
            gb.run(100_000)
            val outputMessage = serial.asByteArray().decodeToString()
            if (outputMessage.contains("Passed") || outputMessage.contains("Failed")) {
                break
            }
        }

        val outputMessage = serial.asByteArray().decodeToString()
        assertContains(
            outputMessage,
            "Passed",
            message = "Actual output message: $outputMessage"
        )
    }
}