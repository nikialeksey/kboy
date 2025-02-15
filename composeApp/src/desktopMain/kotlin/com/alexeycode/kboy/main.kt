package com.alexeycode.kboy

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexeycode.kboy.host.DesktopHost
import com.alexeycode.kboy.io.DesktopController

fun main() = application {

    val host = remember { DesktopHost() }
    val extController = remember { DesktopController() }

    Window(
        onKeyEvent = extController::onKeyEvent,
        onCloseRequest = ::exitApplication,
        title = "KBoy",
    ) {
        App(
            host = host,
            extController = extController
        )
    }
}