package com.alexeycode.kboy

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import com.alexeycode.kboy.host.IosRomFile
import com.alexeycode.kboy.host.IosTime
import com.alexeycode.kboy.host.SimpleRoms
import com.alexeycode.kboy.ui.FileDialog

fun MainViewController() = ComposeUIViewController {
    val time = remember { IosTime() }
    val roms = remember { SimpleRoms() }
    var isSelectRom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        roms.selectRomEvents().collect {
            isSelectRom = true
        }
    }

    App(
        roms = roms,
        time = time
    )
    if (isSelectRom) {
        FileDialog(
            onFilePicked = { url ->
                roms.selectedRom(IosRomFile(url))
            },
            onCompleted = {
                isSelectRom = false
            }
        )
    }
}