package com.alexeycode.kboy

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun readBytes(fileName: String): ByteArray {
    val name = "./src/commonMain/resources/$fileName"

    val path = NSBundle.mainBundle.pathForResource(
        name = name.substringBeforeLast("."),
        ofType = name.substringAfterLast(".", "")
    ) ?: error("Resource not found: $name")

    val data = NSData.dataWithContentsOfFile(path)
        ?: error("Unable to load resource: $name")

    return ByteArray(data.length.toInt()).apply {
        usePinned { pinned ->
            memcpy(
                pinned.addressOf(0),
                data.bytes,
                data.length
            )
        }
    }
}