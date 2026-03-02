package com.alexeycode.kboy.host.io

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.w3c.dom.events.KeyboardEvent

class WebController : Controller, KeyListener {

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

    override fun onKeyEvent(event: KeyboardEvent, isDown: Boolean) {
        println("event.key = ${event.code}")
        when (event.code) {
            "ArrowRight" -> buttonRight.tryEmit(isDown)
            "ArrowLeft" -> buttonLeft.tryEmit(isDown)
            "ArrowUp" -> buttonUp.tryEmit(isDown)
            "ArrowDown" -> buttonDown.tryEmit(isDown)

            "KeyZ" -> buttonA.tryEmit(isDown)
            "KeyX" -> buttonB.tryEmit(isDown)
            "Space" -> buttonSelect.tryEmit(isDown)
            "Enter" -> buttonStart.tryEmit(isDown)
        }
    }
}
