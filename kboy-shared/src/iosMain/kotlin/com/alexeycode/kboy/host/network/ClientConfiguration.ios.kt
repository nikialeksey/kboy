package com.alexeycode.kboy.host.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun platformClientEngineFactory(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return Darwin
}