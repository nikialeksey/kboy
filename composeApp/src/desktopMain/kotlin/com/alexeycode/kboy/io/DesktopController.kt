package com.alexeycode.kboy.io

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class DesktopController : Controller, KeyListener {

    private val buttonRight = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonLeft = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonUp = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonDown = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    private val buttonA = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonB = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonSelect = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val buttonStart = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    override fun right() = buttonRight
    override fun left() = buttonLeft
    override fun up() = buttonUp
    override fun down() = buttonDown

    override fun a() = buttonA
    override fun b() = buttonB
    override fun select() = buttonSelect
    override fun start() = buttonStart

    override fun onKeyEvent(event: KeyEvent): Boolean {
        return when (event.key) {
            Key.DirectionRight -> buttonRight.tryEmit(event.type == KeyEventType.KeyDown)
            Key.DirectionLeft -> buttonLeft.tryEmit(event.type == KeyEventType.KeyDown)
            Key.DirectionUp -> buttonUp.tryEmit(event.type == KeyEventType.KeyDown)
            Key.DirectionDown -> buttonDown.tryEmit(event.type == KeyEventType.KeyDown)

            Key.Z -> buttonA.tryEmit(event.type == KeyEventType.KeyDown)
            Key.X -> buttonB.tryEmit(event.type == KeyEventType.KeyDown)
            Key.Spacebar -> buttonSelect.tryEmit(event.type == KeyEventType.KeyDown)
            Key.Enter -> buttonStart.tryEmit(event.type == KeyEventType.KeyDown)
            else -> false
        }
    }

}