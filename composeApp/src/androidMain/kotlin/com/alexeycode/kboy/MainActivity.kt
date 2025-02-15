package com.alexeycode.kboy

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alexeycode.kboy.host.AndroidHost
import com.alexeycode.kboy.host.AndroidRoms
import com.alexeycode.kboy.io.AndroidFileSystem
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.LoadRomContract
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val host = AndroidHost()
    private val roms = AndroidRoms()
    private val loadRom = registerForActivityResult(LoadRomContract(), roms)

    private val fileSystem = AndroidFileSystem(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareActivityWindow()
        lifecycleScope.launch {
            roms.startRomSelection().collect {
                loadRom.launch(Unit)
            }
        }

        setContent {
            App(
                host = host,
                roms = roms,
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
