package pro.devdesign.gameboy.cpu.opcodes

import com.alexeycode.kboy.gb.cpu.opcodes.GbOpcodes
import com.alexeycode.kboy.lib.Res
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalResourceApi::class)
class GbOpcodesTest {
    @Test
    fun checkInstructionRepresentation() = runTest {
        assertEquals(
            "LD       a16, SP",
            GbOpcodes(Res.readBytes("files/Opcodes.json"))
                .instructionMeta(0x08)
                .toString()
        )
    }
}