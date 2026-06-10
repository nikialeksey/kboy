package com.alexeycode.kboy.host.io

import androidx.compose.ui.input.key.KeyEvent

interface KeyListener {
    fun onKeyEvent(event: KeyEvent): Boolean
}
