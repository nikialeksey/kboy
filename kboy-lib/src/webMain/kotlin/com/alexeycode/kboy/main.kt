package com.alexeycode.kboy

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.alexeycode.kboy.host.WebHost
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val host = WebHost()
    ComposeViewport(document.body!!) {
        App(
            host = host
        )
    }
}