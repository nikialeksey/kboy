package com.alexeycode.kboy

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.alexeycode.kboy.main.Main
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Main()
    }
}