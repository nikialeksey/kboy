package com.alexeycode.kboy.host.io

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface Controller {
    fun right(): Flow<Boolean>
    fun left(): Flow<Boolean>
    fun up(): Flow<Boolean>
    fun down(): Flow<Boolean>

    fun a(): Flow<Boolean>
    fun b(): Flow<Boolean>
    fun select(): Flow<Boolean>
    fun start(): Flow<Boolean>

    class Dummy : Controller {
        override fun right() = emptyFlow<Boolean>()
        override fun left() = emptyFlow<Boolean>()
        override fun up() = emptyFlow<Boolean>()
        override fun down() = emptyFlow<Boolean>()
        override fun a() = emptyFlow<Boolean>()
        override fun b() = emptyFlow<Boolean>()
        override fun select() = emptyFlow<Boolean>()
        override fun start() = emptyFlow<Boolean>()
    }
}