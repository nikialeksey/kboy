package com.alexeycode.kboy.host.network

interface DataMessage {
    fun data(): Int
    fun pkt(): Int
}
