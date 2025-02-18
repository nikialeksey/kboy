package com.alexeycode.kboy.previews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexeycode.kboy.io.TouchControllerListener
import com.alexeycode.kboy.main.GbScreenWithController
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
        GbScreenWithController(
            modifier = Modifier.fillMaxSize(),
            isGameRunning = false,
            screen = emptyFlow(),
            touchControllerListener = TouchControllerListener.Dummy(),
            onSelectRomClicked = {}
        )
    }
}