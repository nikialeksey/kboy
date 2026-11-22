package com.alexeycode.kboy.host.network

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngine.Configuration
import io.ktor.server.engine.ApplicationEngineFactory

expect fun platformServerEngineFactory(): ApplicationEngineFactory<ApplicationEngine, out Configuration>
