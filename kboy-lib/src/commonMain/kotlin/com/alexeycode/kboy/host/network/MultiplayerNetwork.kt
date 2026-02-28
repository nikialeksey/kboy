package com.alexeycode.kboy.host.network

import com.alexeycode.kboy.host.network.multiplayer.Host
import kotlinx.coroutines.flow.Flow

/**
 * In the network of kboy hosts one must be a main.
 * The question is how to negotiate it.
 *
 * Suppose, we have kboy host A. This host knows that we have N-1 other hosts.
 * (A also is a host.)
 *
 * We have two cases:
 * 1. A is main
 * 2. A is not main
 *
 * A is not main
 * In that case A should be able to understand who is the main.
 * A can declare himself as a main.
 *
 * A is main
 * In that case A must answer other hosts that he is main.
 * A can not declare himself as not main.
 *
 *
 * How to implement it.
 * Every host will declare a service in mdns.
 * Every host will look for other services.
 *
 * When A is not main, he will just connect to every service and ask: are you main?
 * First one who answers yes will be marked as main for A host.
 * When A is not main and he wants to declare himself as main, he just need
 * to connect to every service and send a message: I am main.
 *
 * When A is main, he must answer on queries: are you main? and change himself to
 * not main, if somebody will send message: I am main.
 *
 * That is: every KBoy host should implement a server with these two API requests:
 *
 * - Are you main?
 * - I am main!
 *
 */
interface MultiplayerNetwork {

    fun start()

    fun hosts(): Flow<List<Host>>

    fun stop()
}