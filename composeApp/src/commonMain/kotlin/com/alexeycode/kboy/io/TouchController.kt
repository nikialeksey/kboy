package com.alexeycode.kboy.io

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class TouchController : Controller, TouchControllerListener {
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

    override fun rightPressed() {
        buttonRight.tryEmit(true)
    }

    override fun rightReleased() {
        buttonRight.tryEmit(false)
    }

    override fun leftPressed() {
        buttonLeft.tryEmit(true)
    }

    override fun leftReleased() {
        buttonLeft.tryEmit(false)
    }

    override fun upPressed() {
        buttonUp.tryEmit(true)
    }

    override fun upReleased() {
        buttonUp.tryEmit(false)
    }

    override fun downPressed() {
        buttonDown.tryEmit(true)
    }

    override fun downReleased() {
        buttonDown.tryEmit(false)
    }

    override fun aPressed() {
        buttonA.tryEmit(true)
    }

    override fun aReleased() {
        buttonA.tryEmit(false)
    }

    override fun bPressed() {
        buttonB.tryEmit(true)
    }

    override fun bReleased() {
        buttonB.tryEmit(false)
    }

    override fun selectPressed() {
        buttonSelect.tryEmit(true)
    }

    override fun selectReleased() {
        buttonSelect.tryEmit(false)
    }

    override fun startPressed() {
        buttonStart.tryEmit(true)
    }

    override fun startReleased() {
        buttonStart.tryEmit(false)
    }


}