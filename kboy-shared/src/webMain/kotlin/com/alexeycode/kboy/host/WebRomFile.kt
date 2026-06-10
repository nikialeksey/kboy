package com.alexeycode.kboy.host

import kotlinx.io.IOException
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WebRomFile(
    private val file: File
) : RomFile {
    override suspend fun read(): ByteArray {
        return suspendCoroutine { continuation ->
            val reader = FileReader()
            reader.onload = { event ->
                try {
                    val arrayBuffer = event
                        .target
                        ?.unsafeCast<FileReader>()
                        ?.result
                        ?.unsafeCast<ArrayBuffer>()
                        ?: throw IOException("Could not read file")

                    val bytes = Uint8Array(arrayBuffer)
                    val byteArray = ByteArray(bytes.length)
                    for (i in 0 until bytes.length) {
                        byteArray[i] = bytes[i]
                    }

                    // Return the ByteArray
                    continuation.resume(byteArray)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
            reader.readAsArrayBuffer(file)
        }
    }

}