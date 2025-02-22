package com.alexeycode.kboy.ui.gl

import androidx.compose.runtime.Composable
import com.alexeycode.kboy.gb.ppu.Screen

@Composable
expect fun RenderScreen(image: Screen)