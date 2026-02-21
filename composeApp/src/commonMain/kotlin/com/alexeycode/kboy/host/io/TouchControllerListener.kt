package com.alexeycode.kboy.host.io

interface TouchControllerListener {
    fun rightPressed()
    fun rightReleased()
    fun leftPressed()
    fun leftReleased()
    fun upPressed()
    fun upReleased()
    fun downPressed()
    fun downReleased()

    fun aPressed()
    fun aReleased()
    fun bPressed()
    fun bReleased()
    fun selectPressed()
    fun selectReleased()
    fun startPressed()
    fun startReleased()

    class Dummy : TouchControllerListener {
        override fun rightPressed() = Unit
        override fun rightReleased() = Unit
        override fun leftPressed() = Unit
        override fun leftReleased() = Unit
        override fun upPressed() = Unit
        override fun upReleased() = Unit
        override fun downPressed() = Unit
        override fun downReleased() = Unit
        override fun aPressed() = Unit
        override fun aReleased() = Unit
        override fun bPressed() = Unit
        override fun bReleased() = Unit
        override fun selectPressed() = Unit
        override fun selectReleased() = Unit
        override fun startPressed() = Unit
        override fun startReleased() = Unit
    }
}