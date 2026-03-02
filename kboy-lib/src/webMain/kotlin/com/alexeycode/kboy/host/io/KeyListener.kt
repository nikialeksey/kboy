package com.alexeycode.kboy.host.io

import org.w3c.dom.events.KeyboardEvent

interface KeyListener {
    fun onKeyEvent(event: KeyboardEvent, isDown: Boolean)
}
