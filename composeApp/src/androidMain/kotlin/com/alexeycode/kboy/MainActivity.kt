package com.alexeycode.kboy

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.alexeycode.kboy.host.AndroidHost
import com.alexeycode.kboy.io.AndroidFileSystem
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.LoadRomContract

class MainActivity : ComponentActivity() {

    private val loadRom = registerForActivityResult(LoadRomContract()) { romUri: Uri? ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareActivityWindow()
        setContent {
            val host = remember { AndroidHost() }
            val fileSystem = remember { AndroidFileSystem(this) }
            App(
                host = host,
                fileSystem = fileSystem,
                extController = Controller.Dummy()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        prepareActivityWindow()
    }

    private fun prepareActivityWindow() {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}
