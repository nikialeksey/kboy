package com.alexeycode.kboy

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.alexeycode.kboy.host.IosTime

fun MainViewController() = ComposeUIViewController {
    val time = remember { IosTime() }
    App(
        time = time
    )
}