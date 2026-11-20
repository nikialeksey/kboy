package com.alexeycode.kboy.host.network

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.netty.Netty

actual fun platformServerEngineFactory(): ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration> {
    return Netty
}