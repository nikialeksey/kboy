package com.alexeycode.kboy.gb.cpu

import com.alexeycode.kboy.gb.cpu.interrupts.GbInterrupts
import com.alexeycode.kboy.gb.cpu.registers.GbRegisters
import com.alexeycode.kboy.gb.mem.SimpleMemory
import com.goncalossilva.resources.Resource
import com.goncalossilva.resources.ResourceReadException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.minutes

/**
 * https://github.com/adtennant/GameboyCPUTests/
 */
class GbCpuTest {

    @Test
    fun test() = runTest(timeout = 5.minutes) {
        val r = GbRegisters()
        val memSize = 0xffff + 1
        val mem = SimpleMemory(Array(memSize) { 0 })
        val interrupts = GbInterrupts()
        val cpu = GbCpu(r, mem, interrupts)

        for (testCaseNumber in 0x00..0xff) {
            val caseFileName = "${testCaseNumber.toHexString().takeLast(2)}.json"
            val caseBytes = try {
                Resource("files/cpu-tests/$caseFileName").readBytes()
            } catch (ignored: ResourceReadException) {
                continue
            }
            val caseVariants = Json.parseToJsonElement(caseBytes.decodeToString())
            assertIs<JsonArray>(caseVariants)
            for (variant in caseVariants) {
                assertIs<JsonObject>(variant)
                val variantName = variant["name"]!!

                val initialState = variant["initial"]!!.jsonObject
                r.a().set(initialState["a"]!!.jsonPrimitive.int)
                r.b().set(initialState["b"]!!.jsonPrimitive.int)
                r.c().set(initialState["c"]!!.jsonPrimitive.int)
                r.d().set(initialState["d"]!!.jsonPrimitive.int)
                r.e().set(initialState["e"]!!.jsonPrimitive.int)
                r.f().set(initialState["f"]!!.jsonPrimitive.int)
                r.h().set(initialState["h"]!!.jsonPrimitive.int)
                r.l().set(initialState["l"]!!.jsonPrimitive.int)
                r.pc().set(initialState["pc"]!!.jsonPrimitive.int - 1)
                r.sp().set(initialState["sp"]!!.jsonPrimitive.int)

                for (address in 0 until memSize) mem.write8(address, 0)
                for (ramState in initialState["ram"]!!.jsonArray) {
                    val address = ramState.jsonArray[0].jsonPrimitive.int
                    val value = ramState.jsonArray[1].jsonPrimitive.int
                    mem.write8(address, value)
                }

                cpu.tick()

                val finalState = variant["final"]!!.jsonObject

                val commonMessage = "Case name: $caseFileName, variant name: $variantName"
                val incorrectRegMessage = "Incorrect register value. $commonMessage, register:"
                assertEquals(finalState["a"]!!.jsonPrimitive.int, r.a().get(), "$incorrectRegMessage a")
                assertEquals(finalState["b"]!!.jsonPrimitive.int, r.b().get(), "$incorrectRegMessage b")
                assertEquals(finalState["c"]!!.jsonPrimitive.int, r.c().get(), "$incorrectRegMessage c")
                assertEquals(finalState["d"]!!.jsonPrimitive.int, r.d().get(), "$incorrectRegMessage d")
                assertEquals(finalState["e"]!!.jsonPrimitive.int, r.e().get(), "$incorrectRegMessage e")
                assertEquals(finalState["f"]!!.jsonPrimitive.int, r.f().get(), "$incorrectRegMessage f")
                assertEquals(finalState["h"]!!.jsonPrimitive.int, r.h().get(), "$incorrectRegMessage h")
                assertEquals(finalState["l"]!!.jsonPrimitive.int, r.l().get(), "$incorrectRegMessage l")
                assertEquals(finalState["pc"]!!.jsonPrimitive.int - 1, r.pc().get(), "$incorrectRegMessage pc")
                assertEquals(finalState["sp"]!!.jsonPrimitive.int, r.sp().get(), "$incorrectRegMessage sp")

                val incorrectMemMessage = "Incorrect memory value. $commonMessage, address:"
                for (ramState in finalState["ram"]!!.jsonArray) {
                    val address = ramState.jsonArray[0].jsonPrimitive.int
                    val value = ramState.jsonArray[1].jsonPrimitive.int
                    assertEquals(value, mem.read8(address), "$incorrectMemMessage $address")
                }
            }
        }
    }
}
