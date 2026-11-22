package com.alexeycode.kboy.host.network

import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SimpleSocketSession(
    private val webSocketSession: WebSocketSession,
    private val scope: CoroutineScope,
    private val logTag: String,
) : SocketSession {

    private val ackListeners = mutableListOf<(Int) -> Unit>()
    private val sending = Channel<String>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var buffer: SocketInputSession.Buffer = SocketInputSession.Buffer.Empty

    override suspend fun start() {
        println("!!!!!!!!! $logTag session started")
        scope.launch {
            for (message in sending) {
                println("!!!!!!! sending message: $message")
                webSocketSession.send(message)
                println("!!!!!!! message sent")
            }
        }
        scope.launch {
            for (incomingFrame in webSocketSession.incoming) {
                println("!!!!!!!!!! received frame: $incomingFrame")
                val message = incomingFrame as? Frame.Text ?: continue

                val messageText = message.readText()
                println("!!!!!!!!!! received message: $messageText")
                if (messageText.startsWith("Ack: ")) {
                    messageText.substringAfter("Ack: ").toIntOrNull()?.let { ackPkt ->
                        for (ackListener in ackListeners) {
                            ackListener(ackPkt)
                        }
                    }
                } else if (messageText.contains("Data: ")) {
                    buffer = StringDataBuffer(StringDataMessage(messageText))
                }
            }
        }
    }

    override fun stop() {
        // ignored
    }

    override fun buffer(): SocketInputSession.Buffer {
        return buffer
    }

    override fun clearBuffer() {
        buffer = SocketInputSession.Buffer.Empty
    }

    override fun sendAck(pkt: Int) {
        sending.trySend("Ack: $pkt")
    }

    override fun send(data: Int, pkt: Int) {
        sending.trySend("Id: $pkt, Data: $data")
    }

    override fun addAckListener(listener: (Int) -> Unit) {
        ackListeners.add(listener)
    }

    override fun removeAckListener(listener: (Int) -> Unit) {
        ackListeners.remove(listener)
    }
}
