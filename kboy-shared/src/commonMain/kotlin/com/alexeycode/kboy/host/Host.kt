package com.alexeycode.kboy.host

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface Host {
    fun externalControllerAvailable(): Flow<Boolean>

    class Dummy : Host {
        override fun externalControllerAvailable(): Flow<Boolean> {
            return flowOf(false)
        }
    }
}