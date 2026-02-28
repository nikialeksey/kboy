package com.alexeycode.kboy.ui.gl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import com.alexeycode.kboy.gb.ppu.Screen
import java.nio.ByteBuffer


@Composable
actual fun RenderScreen(image: Screen) {
    val bitmap = remember {
        createBitmap(image.width(), image.height())
    }

    LaunchedEffect(image) {
        bitmap.copyPixelsFromBuffer(
            ByteBuffer.wrap(image.pixels())
        )
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        filterQuality = FilterQuality.None
    )
}
