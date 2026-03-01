package com.alexeycode.kboy.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alexeycode.kboy.host.io.TouchControllerListener
import com.alexeycode.kboy.screens.main.MainScreen
import com.alexeycode.kboy.ui.DarkColors
import kotlinx.coroutines.flow.emptyFlow

@Preview(
    widthDp = 800,
    heightDp = 420
)
@Composable
fun ScreenWithController() {
    MaterialTheme(
        colorScheme = DarkColors
    ) {
        MainScreen(
            isTouchEnabled = true,
            isGameRunning = false,
            screen = emptyFlow(),
            touchListener = TouchControllerListener.Dummy(),
            onSelectRomClicked = {}
        )
    }
}
