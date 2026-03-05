package com.alexeycode.kboy.host.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.alexeycode.kboy.host.network.multiplayer.Host
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val TAG = "AndroidMultiplayerNetwork"

class AndroidMultiplayerNetwork(
    private val nsdManager: NsdManager,
) : MultiplayerNetwork {

    private val hosts: MutableSet<Host> = mutableSetOf()
    private val hostsLock = ReentrantLock()
    private val hostsFlow = MutableSharedFlow<Set<Host>>(1, 0, BufferOverflow.DROP_OLDEST)
    private val callbackExecutor = Executors.newSingleThreadExecutor()
    private val infoListeners = mutableListOf<ServiceInfoListener>()

    private val discoverListener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(serviceType: String) {
            Log.d(TAG, "Started discovery services by type: $serviceType")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.d(TAG, "Stopped discovery services by type: $serviceType")
        }

        override fun onServiceFound(info: NsdServiceInfo) {
            Log.d(TAG, "Services found: $info. Subscribe for updates...")
            subscribeServiceUpdates(info)
        }

        override fun onServiceLost(info: NsdServiceInfo) {
            Log.d(TAG, "Service lost: $info")
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
            Log.d(TAG, "Start discovery by type $serviceType failed with error $errorCode")
        }

        override fun onStopDiscoveryFailed(
            serviceType: String,
            errorCode: Int
        ) {
            Log.d(TAG, "Stop discovery by type $serviceType failed with error $errorCode")
        }
    }

    override fun start() {
        Log.d(TAG, "Start network...")
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
        Log.d(TAG, "Stop network...")
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

        override fun onServiceInfoCallbackRegistrationFailed(errorCode: Int) {
            Log.d(
                TAG,
                "Service info callback (${System.identityHashCode(this)}) registration failed with code $errorCode"
            )
        }

        override fun onServiceInfoCallbackUnregistered() {
            Log.d(TAG, "Service info callback (${System.identityHashCode(this)}) unregistered")
            hostsLock.withLock {
                hosts.removeAll { it.port == port && it.address == address }
                hostsFlow.tryEmit(hosts.toSet())
            }
        }

        override fun onServiceLost() {
            Log.d(TAG, "Service info callback (${System.identityHashCode(this)}) registered service loss")
            hostsLock.withLock {
                hosts.removeAll { it.port == port && it.address == address }
                hostsFlow.tryEmit(hosts.toSet())
            }
        }

        override fun onServiceUpdated(info: NsdServiceInfo) {
            Log.d(
                TAG,
                "Service info callback (${System.identityHashCode(this)}) registered service update, info: $info"
            )
            hosts.removeAll { it.port == port && it.address == address }
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
