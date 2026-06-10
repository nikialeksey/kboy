package com.alexeycode.kboy.host

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.posix.memcpy

class IosRomFile(
    private val url: NSURL
) : RomFile {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun read(): ByteArray {
        val data = NSData.dataWithContentsOfURL(url)
        return ByteArray(data!!.length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), data.bytes, data.length)
            }
        }
    }
}