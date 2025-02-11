package com.alexeycode.kboy

import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexeycode.kboy.io.Controller
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun main() = application {
    val buttonRight = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonLeft = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonUp = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonDown = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }

    val buttonA = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonB = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonSelect = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }
    val buttonStart = remember { MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST) }

    val controller = remember {
        object : Controller {
            override fun right() = buttonRight
            override fun left() = buttonLeft
            override fun up() = buttonUp
            override fun down() = buttonDown

            override fun a() = buttonA
            override fun b() = buttonB
            override fun select() = buttonSelect
            override fun start() = buttonStart
        }
    }

    Window(
        onKeyEvent = { event ->
            when (event.key) {
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
        },
        onCloseRequest = ::exitApplication,
        title = "KBoy",
    ) {
        App(controller)
    }
}