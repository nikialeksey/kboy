package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.host.network.multiplayer.Host
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class DesktopMultiplayerNetworkTest {
    @Test
    @Ignore
    fun asdasd() = runTest {
        val host1 = DesktopMultiplayerNetwork()
        val host2 = DesktopMultiplayerNetwork()

        var host1Hosts = setOf<Host>()
        var host2Hosts = setOf<Host>()
        launch { host1.hosts().collect { hosts -> host1Hosts = hosts } }
        launch { host2.hosts().collect { hosts -> host2Hosts = hosts } }

        try {
            host1.start()
            host2.start()

            delay(3000)

            assertEquals(1, host1Hosts.size)
            assertEquals(1, host2Hosts.size)
        } finally {
            host1.stop()
            host2.stop()
        }
    }
}
