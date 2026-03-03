package com.alexeycode.kboy

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.alexeycode.kboy.host.SimpleRoms
import com.alexeycode.kboy.host.WebHost
import com.alexeycode.kboy.host.WebRomFile
import com.alexeycode.kboy.host.WebTime
import com.alexeycode.kboy.host.io.WebController
import com.alexeycode.kboy.ui.FileDialog
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body!!

    val host = WebHost()
    val roms = SimpleRoms()
    val time = WebTime(window.performance)
    val controller = WebController()
    body.onkeydown = { controller.onKeyEvent(it, true) }
    body.onkeyup = { controller.onKeyEvent(it, false) }

    ComposeViewport(body) {
        var isSelectRom by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            roms.selectRomEvents().collect {
                isSelectRom = true
            }
        }

        App(
            host = host,
            roms = roms,
            time = time,
            extController = controller
        )

        if (isSelectRom) {
            FileDialog(
                onFilePicked = { file ->
                    roms.selectedRom(WebRomFile(file))
                },
                onCompleted = {
                    isSelectRom = false
                }
            )
        }
    }
}