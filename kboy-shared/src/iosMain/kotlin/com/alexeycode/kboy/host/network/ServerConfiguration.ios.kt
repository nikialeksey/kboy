package com.alexeycode.kboy.host.network

import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory

actual fun platformServerEngineFactory(): ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration> {
    return CIO
}