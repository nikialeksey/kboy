package com.alexeycode.kboy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.alexeycode.kboy.host.AndroidHost
import com.alexeycode.kboy.io.Controller

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val host = remember { AndroidHost() }
            App(
                host = host,
                extController = Controller.Dummy()
            )
        }
    }
}
