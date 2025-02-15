package com.alexeycode.kboy

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexeycode.kboy.host.DesktopHost
import com.alexeycode.kboy.host.DesktopRoms
import com.alexeycode.kboy.io.DesktopController
import com.alexeycode.kboy.io.DesktopFileSystem

fun main() = application {

    val host = remember { DesktopHost() }
    val roms = remember { DesktopRoms() }
    val fileSystem = remember { DesktopFileSystem() }
    val extController = remember { DesktopController() }

    Window(
        onKeyEvent = extController::onKeyEvent,
        onCloseRequest = ::exitApplication,
        title = "KBoy",
    ) {
        App(
            host = host,
            roms = roms,
            fileSystem = fileSystem,
            extController = extController
        )
    }
}