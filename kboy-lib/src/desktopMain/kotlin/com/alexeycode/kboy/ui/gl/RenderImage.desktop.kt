package com.alexeycode.kboy.ui.gl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.alexeycode.kboy.gb.ppu.Screen
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Rect

@Composable
actual fun RenderScreen(image: Screen) {
    var localImage by remember { mutableStateOf(image) }
    val bitmap = remember {
        Bitmap().apply {
            allocPixels(
                ImageInfo(
                    ColorInfo(
                        ColorType.RGBA_8888,
                        ColorAlphaType.OPAQUE,
                        ColorSpace.sRGB
                    ),
                    image.width(),
                    image.height()
                )
            )
        }
    }

    LaunchedEffect(image) {
        localImage = image
        bitmap.installPixels(
            bitmap.imageInfo,
            image.pixels(),
            image.width() * 4
        )
    }

    Canvas(Modifier.fillMaxSize()) {
        drawIntoCanvas {
            val workaround = localImage.width()
            it.nativeCanvas.drawImageRect(
                Image.makeFromBitmap(bitmap),
                Rect.makeWH(size.width, size.height)
            )
        }
    }
}
