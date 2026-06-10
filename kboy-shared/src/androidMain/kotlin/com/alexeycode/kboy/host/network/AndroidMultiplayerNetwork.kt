package com.alexeycode.kboy.host.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.alexeycode.kboy.host.network.multiplayer.Host
import com.alexeycode.kboy.log.Log
import com.alexeycode.kboy.log.Network
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class AndroidMultiplayerNetwork(
    private val nsdManager: NsdManager,
    private val log: Log
) : MultiplayerNetwork {

    private val hosts: MutableSet<Host> = mutableSetOf()
    private val hostsLock = ReentrantLock()
    private val hostsFlow = MutableSharedFlow<Set<Host>>(1, 0, BufferOverflow.DROP_OLDEST)
    private val callbackExecutor = Executors.newSingleThreadExecutor()
    private val infoListeners = mutableListOf<ServiceInfoListener>()

    private val discoverListener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(serviceType: String) {
            log.d(Network, "Started discovery services by type: %s", serviceType)
        }

        override fun onDiscoveryStopped(serviceType: String) {
            log.d(Network, "Stopped discovery services by type: %s", serviceType)
        }

        override fun onServiceFound(info: NsdServiceInfo) {
            log.d(Network, "Services found: %s. Subscribe for updates...", info)
            subscribeServiceUpdates(info)
        }

        override fun onServiceLost(info: NsdServiceInfo) {
            log.d(Network, "Service lost: %s", info)
            val address = info.hostAddresses.firstOrNull()?.hostAddress
            hostsLock.withLock {
                hosts.removeAll { it.port == info.port && it.address == address }
                hostsFlow.tryEmit(hosts.toSet())
            }
        }

        override fun onStartDiscoveryFailed(
            serviceType: String,
            errorCode: Int
        ) {
            log.d(Network, "Start discovery by type %s failed with error %s", serviceType, errorCode)
        }

        override fun onStopDiscoveryFailed(
            serviceType: String,
            errorCode: Int
        ) {
            log.d(Network, "Stop discovery by type %s failed with error %s", serviceType, errorCode)
        }
    }

    override fun start() {
        log.d(Network, "Start network...")
        nsdManager.discoverServices(
            "_kboy._tcp",
            NsdManager.PROTOCOL_DNS_SD,
            discoverListener
        )
    }

    override fun hosts(): Flow<Set<Host>> {
        return hostsFlow.distinctUntilChanged()
    }

    override fun stop() {
        log.d(Network, "Stop network...")
        for (listener in infoListeners) {
            nsdManager.unregisterServiceInfoCallback(listener)
        }
        nsdManager.stopServiceDiscovery(discoverListener)
    }

    private fun subscribeServiceUpdates(info: NsdServiceInfo) {
        val listener = ServiceInfoListener()
        infoListeners.add(listener)
        nsdManager.registerServiceInfoCallback(
            info,
            callbackExecutor,
            listener
        )
    }

    private inner class ServiceInfoListener : NsdManager.ServiceInfoCallback {
        private var address: String? = null
        private var port: Int? = null
        private val identity: Int by lazy {
            System.identityHashCode(this)
        }

        override fun onServiceInfoCallbackRegistrationFailed(errorCode: Int) {
            log.d(
                Network,
                "Service info callback (%d) registration failed with code %d",
                identity,
                errorCode
            )
        }

        override fun onServiceInfoCallbackUnregistered() {
            log.d(Network, "Service info callback (%d) unregistered", identity)
            hostsLock.withLock {
                hosts.removeAll { it.port == port && it.address == address }
                hostsFlow.tryEmit(hosts.toSet())
            }
        }

        override fun onServiceLost() {
            log.d(Network, "Service info callback (%d) registered service loss", identity)
            hostsLock.withLock {
                hosts.removeAll { it.port == port && it.address == address }
                hostsFlow.tryEmit(hosts.toSet())
            }
        }

        override fun onServiceUpdated(info: NsdServiceInfo) {
            log.d(
                Network,
                "Service info callback (%d) registered service update, info: %s",
                identity,
                info
            )
            val updatedAddress = info.hostAddresses.firstOrNull()?.hostAddress
            val updatedPort = info.port
            if (updatedAddress != null && (address != updatedAddress || port != updatedPort)) {
                hostsLock.withLock {
                    hosts.removeAll { it.port == port && it.address == address }
                    hosts.add(Host(updatedAddress, updatedPort))
                    address = updatedAddress
                    port = updatedPort
                    hostsFlow.tryEmit(hosts.toSet())
                }
            }
        }
    }
}
