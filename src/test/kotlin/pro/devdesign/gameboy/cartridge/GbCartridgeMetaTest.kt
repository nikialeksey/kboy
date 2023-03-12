package pro.devdesign.gameboy.cartridge

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GbCartridgeMetaTest {

    @ParameterizedTest
    @CsvSource(value = [
        "/pac-man.gb,PAC-MAN",
        "/snake.gb,SNAKE",
        "/tamagotchi.gb,GB TAMAGOTCHI",
        "/tetris.gb,TETRIS",
        "/ms_pac-man.gb,MS.PAC-MAN"
    ])
    fun name(fileName: String, expectedName: String) {
        Assertions
            .assertThat(
                GbCartridgeMeta(GbCartridgeData(javaClass, fileName))
                    .header()
                    .name()
            )
            .isEqualTo(expectedName)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "/pac-man.gb,0",
        "/snake.gb,2",
        "/tamagotchi.gb,8",
        "/tetris.gb,0",
        "/ms_pac-man.gb,0"
    ])
    fun ram(fileName: String, expectedRam: Int) {
        Assertions
            .assertThat(
                GbCartridgeMeta(GbCartridgeData(javaClass, fileName))
                    .ramSizeKb()
            )
            .isEqualTo(expectedRam)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "/pac-man.gb,64",
        "/snake.gb,32",
        "/tamagotchi.gb,512",
        "/tetris.gb,32",
        "/ms_pac-man.gb,64"
    ])
    fun rom(fileName: String, expectedRom: Int) {
        Assertions
            .assertThat(
                GbCartridgeMeta(GbCartridgeData(javaClass, fileName))
                    .romSizeKb()
            )
            .isEqualTo(expectedRom)
    }
}