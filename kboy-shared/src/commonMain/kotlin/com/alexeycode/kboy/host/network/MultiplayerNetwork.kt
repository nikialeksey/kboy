package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.host.network.multiplayer.Host
import kotlinx.coroutines.flow.Flow

interface MultiplayerNetwork {

    fun start()

    fun hosts(): Flow<Set<Host>>

    fun stop()
}