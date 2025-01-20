package com.alexeycode.kboy.gb.cartridge

import kboy.composeapp.generated.resources.Res
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalResourceApi::class)
class GbCartridgeMetaTest {

    @Test
    fun testPacManName() = runTest {
        testName("pac-man.gb", "PAC-MAN")
    }

    @Test
    fun testSnakeName() = runTest {
        testName("snake.gb", "SNAKE")
    }

    @Test
    fun testTamagotchiName() = runTest {
        testName("tamagotchi.gb", "GB TAMAGOTCHI")
    }

    @Test
    fun testTetrisName() = runTest {
        testName("tetris.gb", "TETRIS")
    }

    @Test
    fun testMsPacManName() = runTest {
        testName("ms_pac-man.gb", "MS.PAC-MAN")
    }

    private suspend fun testName(fileName: String, expectedName: String) {
        assertEquals(
            expectedName,
            GbCartridgeMeta(GbCartridgeData(Res.readBytes("files/$fileName")))
                .header()
                .name()
        )
    }

    @Test
    fun testPacManRam() = runTest {
        testRam("pac-man.gb", 0)
    }

    @Test
    fun testSnakeRam() = runTest {
        testRam("snake.gb", 2)
    }

    @Test
    fun testTamagotchiRam() = runTest {
        testRam("tamagotchi.gb", 8)
    }

    @Test
    fun testTetrisRam() = runTest {
        testRam("tetris.gb", 0)
    }

    @Test
    fun testMsPacManRam() = runTest {
        testRam("ms_pac-man.gb", 0)
    }

    private suspend fun testRam(fileName: String, expectedRam: Int) {
        assertEquals(
            expectedRam,
            GbCartridgeMeta(GbCartridgeData(Res.readBytes("files/$fileName")))
                .ramSizeKb()
        )
    }

    @Test
    fun testPacManRom() = runTest {
        testRom("/pac-man.gb", 64)
    }

    @Test
    fun testSnakeRom() = runTest {
        testRom("/snake.gb", 32)
    }

    @Test
    fun testTamagotchiRom() = runTest {
        testRom("/tamagotchi.gb", 512)
    }

    @Test
    fun testTetrisRom() = runTest {
        testRom("/tetris.gb", 32)
    }

    @Test
    fun testMsPacManRom() = runTest {
        testRom("/ms_pac-man.gb", 64)
    }

    private suspend fun testRom(fileName: String, expectedRom: Int) {
        assertEquals(
            expectedRom,
            GbCartridgeMeta(GbCartridgeData(Res.readBytes("files/$fileName")))
                .romSizeKb()
        )
    }
}