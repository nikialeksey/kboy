package com.alexeycode.kboy.host.network

class StringDataMessage(
    private val raw: String
) : DataMessage {

    override fun data(): Int {
        return raw.substringAfter("Data: ").substringBefore(',').toIntOrNull() ?: 0
    }

    override fun pkt(): Int {
        return raw.substringAfter("Id: ").substringBefore(',').toIntOrNull() ?: 0
    }
}