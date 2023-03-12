package pro.devdesign.gameboy

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import pro.devdesign.gameboy.cartridge.GbCartridgeData
import pro.devdesign.gameboy.cartridge.GbCartridgeMeta
import java.io.File

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(60)
    config.setTitle("GameBoy")
    Lwjgl3Application(
        GameBoy(
            GbCartridgeMeta(
                GbCartridgeData(
                    File("C:\\Users\\nikia\\IdeaProjects\\GameBoy\\src\\test\\resources\\pac-man.gb")
                )
            )
        ),
        config
    )
}