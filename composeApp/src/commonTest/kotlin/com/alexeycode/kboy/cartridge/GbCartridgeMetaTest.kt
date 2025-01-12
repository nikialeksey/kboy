package com.alexeycode.kboy.cartridge

import com.goncalossilva.resources.Resource
import kotlin.test.Test
import kotlin.test.assertEquals

class GbCartridgeMetaTest {

    @Test
    fun testPacManName() {
        testName("pac-man.gb", "PAC-MAN")
    }

    @Test
    fun testSnakeName() {
        testName("snake.gb", "SNAKE")
    }

    @Test
    fun testTamagotchiName() {
        testName("tamagotchi.gb", "GB TAMAGOTCHI")
    }

    @Test
    fun testTetrisName() {
        testName("tetris.gb", "TETRIS")
    }

    @Test
    fun testMsPacManName() {
        testName("ms_pac-man.gb", "MS.PAC-MAN")
    }

    private fun testName(fileName: String, expectedName: String) {
        assertEquals(
            expectedName,
            GbCartridgeMeta(GbCartridgeData(Resource("src/commonTest/resources/$fileName").readBytes()))
                .header()
                .name()
        )
    }

    @Test
    fun testPacManRam() {
        testRam("pac-man.gb", 0)
    }

    @Test
    fun testSnakeRam() {
        testRam("snake.gb", 2)
    }

    @Test
    fun testTamagotchiRam() {
        testRam("tamagotchi.gb", 8)
    }

    @Test
    fun testTetrisRam() {
        testRam("tetris.gb", 0)
    }

    @Test
    fun testMsPacManRam() {
        testRam("ms_pac-man.gb", 0)
    }

    private fun testRam(fileName: String, expectedRam: Int) {
        assertEquals(
            expectedRam,
            GbCartridgeMeta(GbCartridgeData(Resource("src/commonTest/resources/$fileName").readBytes()))
                .ramSizeKb()
        )
    }

    @Test
    fun testPacManRom() {
        testRom("/pac-man.gb", 64)
    }

    @Test
    fun testSnakeRom() {
        testRom("/snake.gb", 32)
    }

    @Test
    fun testTamagotchiRom() {
        testRom("/tamagotchi.gb", 512)
    }

    @Test
    fun testTetrisRom() {
        testRom("/tetris.gb", 32)
    }

    @Test
    fun testMsPacManRom() {
        testRom("/ms_pac-man.gb", 64)
    }

    private fun testRom(fileName: String, expectedRom: Int) {
        assertEquals(
            expectedRom,
            GbCartridgeMeta(GbCartridgeData(Resource("src/commonTest/resources/$fileName").readBytes()))
                .romSizeKb()
        )
    }
}