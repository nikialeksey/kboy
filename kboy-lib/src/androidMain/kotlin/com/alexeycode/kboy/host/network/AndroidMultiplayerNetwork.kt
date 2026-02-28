package com.alexeycode.kboy.host.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.alexeycode.kboy.host.network.multiplayer.Host
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class AndroidMultiplayerNetwork(
    private val nsdManager: NsdManager
) : MultiplayerNetwork {

    init {
        nsdManager.discoverServices("_kboy._tcp", NsdManager.PROTOCOL_DNS_SD, object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(p0: String?) {
                println("!!!!!!!!!!!!!!!!!!! onDiscoveryStarted")
            }

            override fun onDiscoveryStopped(p0: String?) {
                println("!!!!!!!!!!!!!!!!!!! onDiscoveryStopped")
            }

            override fun onServiceFound(p0: NsdServiceInfo?) {
                println("!!!!!!!!!!!!!!!!!!! onServiceFound")
            }

            override fun onServiceLost(p0: NsdServiceInfo?) {
                println("!!!!!!!!!!!!!!!!!!! onServiceLost")
            }

            override fun onStartDiscoveryFailed(
                p0: String?,
                p1: Int
            ) {
                println("!!!!!!!!!!!!!!!!!!! onStartDiscoveryFailed")
            }

            override fun onStopDiscoveryFailed(
                p0: String?,
                p1: Int
            ) {
                println("!!!!!!!!!!!!!!!!!!! onStopDiscoveryFailed")
            }

        })
    }

    override fun start() {
    }

    override fun hosts(): Flow<List<Host>> {
        return emptyFlow()
    }

    override fun stop() {
    }
}