package com.alexeycode.kboy.gl

import androidx.compose.runtime.Composable
import com.alexeycode.kboy.gb.ppu.ImageBitmap

@Composable
expect fun RenderImage(image: ImageBitmap)