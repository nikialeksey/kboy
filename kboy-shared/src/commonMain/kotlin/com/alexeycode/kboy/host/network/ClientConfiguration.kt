package com.alexeycode.kboy.host.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect fun platformClientEngineFactory(): HttpClientEngineFactory<HttpClientEngineConfig>
