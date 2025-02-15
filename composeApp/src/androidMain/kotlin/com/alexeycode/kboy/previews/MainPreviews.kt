package com.alexeycode.kboy.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alexeycode.kboy.io.TouchControllerListener
import com.alexeycode.kboy.main.GbScreenWithController
import kotlinx.coroutines.flow.emptyFlow

@Preview(
    widthDp = 800,
    heightDp = 420
)
@Composable
fun ScreenWithController() {
    GbScreenWithController(
        isGameRunning = false,
        screen = emptyFlow(),
        touchControllerListener = TouchControllerListener.Dummy(),
        onSelectRomClicked = {}
    )
}