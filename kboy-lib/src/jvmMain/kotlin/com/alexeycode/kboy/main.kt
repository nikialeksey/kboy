package com.alexeycode.kboy

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexeycode.kboy.host.DesktopHost
import com.alexeycode.kboy.host.DesktopRoms
import com.alexeycode.kboy.host.DesktopTime
import com.alexeycode.kboy.host.io.DesktopController
import com.alexeycode.kboy.host.io.DesktopFileSystem
import com.alexeycode.kboy.host.network.DesktopMultiplayerNetwork
import com.alexeycode.kboy.ui.FileDialog
import com.alexeycode.kboy.lib.Res
import com.alexeycode.kboy.lib.ic_launcher
import org.jetbrains.compose.resources.painterResource

fun main() = application {

    val multiplayerNetwork = DesktopMultiplayerNetwork()
    multiplayerNetwork.start()

    val host = remember { DesktopHost() }
    val roms = remember { DesktopRoms() }
    val fileSystem = remember { DesktopFileSystem() }
    val time = remember { DesktopTime() }
    val extController = remember { DesktopController() }

    var isSelectRom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        roms.selectRomEvents().collect {
            isSelectRom = true
        }
    }

    Window(
        onKeyEvent = extController::onKeyEvent,
        onCloseRequest = ::exitApplication,
        title = "KBoy",
        icon = painterResource(Res.drawable.ic_launcher)
    ) {
        // to change title bar color:
        // https://www.codespeedy.com/how-to-change-the-color-of-title-bar-in-jframe-in-java/
        App(
            host = host,
            roms = roms,
            fileSystem = fileSystem,
            time = time,
            extController = extController
        )

        if (isSelectRom) {
            FileDialog(onCloseRequest = {
                if (it != null) {
                    roms.selectedRom(it)
                }
                isSelectRom = false
            })
        }
    }
}