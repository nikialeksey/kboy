package com.alexeycode.kboy

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.alexeycode.kboy.host.Host
import com.alexeycode.kboy.host.Roms
import com.alexeycode.kboy.host.Time
import com.alexeycode.kboy.host.Vibrator
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.FileSystem
import com.alexeycode.kboy.screens.main.Main
import com.alexeycode.kboy.ui.DarkColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    host: Host = Host.Dummy(),
    roms: Roms = Roms.Dummy(),
    fileSystem: FileSystem = FileSystem.Dummy(),
    vibrator: Vibrator = Vibrator.Dummy(),
    time: Time = Time.ProgressiveTime(),
    extController: Controller = Controller.Dummy(),
    colorScheme: ColorScheme = DarkColors
) {
    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Main(
            host,
            roms,
            fileSystem,
            vibrator,
            time,
            extController
        )
    }
}