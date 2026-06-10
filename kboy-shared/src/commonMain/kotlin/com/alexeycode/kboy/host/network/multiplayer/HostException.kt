package com.alexeycode.kboy.host.network.multiplayer

class HostException(
    message: String,
    throwable: Throwable? = null
) : Exception(message, throwable)