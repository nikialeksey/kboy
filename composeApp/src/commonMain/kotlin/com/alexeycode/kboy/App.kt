package com.alexeycode.kboy

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.alexeycode.kboy.host.Host
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.FileSystem
import com.alexeycode.kboy.main.Main
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    host: Host = Host.Dummy(),
    fileSystem: FileSystem = FileSystem.Dummy(),
    extController: Controller = Controller.Dummy()
) {
    MaterialTheme {
        Main(
            host,
            fileSystem,
            extController
        )
    }
}