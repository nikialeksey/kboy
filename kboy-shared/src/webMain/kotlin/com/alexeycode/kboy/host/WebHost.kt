package com.alexeycode.kboy.host

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WebHost : Host {
    override fun externalControllerAvailable(): Flow<Boolean> {
        return flowOf(true)
    }
}