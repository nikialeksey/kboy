package com.alexeycode.kboy

import android.content.pm.ActivityInfo
import android.net.nsd.NsdManager
import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.alexeycode.kboy.log.AndroidLog
import com.alexeycode.kboy.log.Network
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val log = AndroidLog()
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
        val multiplayerNetwork = AndroidMultiplayerNetwork(nsdManager, log)
        multiplayerNetwork.start()

        lifecycleScope.launch {
            roms.selectRomEvents().collect {
                loadRom.launch(Unit)
            }
        }

        setContent {
            val hosts by multiplayerNetwork.hosts().collectAsState(emptySet())

            LaunchedEffect(hosts) {
                log.i(Network, "Hosts: %s", hosts)
            }

            val context = LocalContext.current
            val colors = dynamicDarkColorScheme(context)
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
