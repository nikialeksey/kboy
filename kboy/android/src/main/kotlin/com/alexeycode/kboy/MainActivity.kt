package com.alexeycode.kboy

import android.content.pm.ActivityInfo
import android.net.nsd.NsdManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alexeycode.kboy.host.AndroidHost
import com.alexeycode.kboy.host.AndroidTime
import com.alexeycode.kboy.host.AndroidVibrator
import com.alexeycode.kboy.host.SimpleRoms
import com.alexeycode.kboy.host.io.Controller
import com.alexeycode.kboy.host.network.AndroidMultiplayerNetwork
import com.alexeycode.kboy.ui.DarkColors
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val host = AndroidHost()
    private val roms = SimpleRoms()
    private val loadRom = registerForActivityResult(
        LoadRomContract(),
        LoadRomResult({ applicationContext }, roms)
    )

    private val time = AndroidTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareActivityWindow()

        val client = HttpClient(CIO) {
            install(WebSockets) {
                pingIntervalMillis = 20_000
            }
        }
        val nsdManager = getSystemService(NSD_SERVICE) as NsdManager
        val multiplayerNetwork = AndroidMultiplayerNetwork(nsdManager, client)

        lifecycleScope.launch {
            roms.selectRomEvents().collect {
                loadRom.launch(Unit)
            }
        }

        setContent {
            val context = LocalContext.current
            val colors = when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
                    dynamicDarkColorScheme(context)
                }
                else -> {
                    DarkColors
                }
            }
            val vibrator = remember(context) {
                AndroidVibrator(context.getSystemService(Vibrator::class.java))
            }

            App(
                host = host,
                roms = roms,
                time = time,
                vibrator = vibrator,
                extController = Controller.Dummy(),
                colorScheme = colors
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
