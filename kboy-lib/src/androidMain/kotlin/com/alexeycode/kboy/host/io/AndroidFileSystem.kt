package com.alexeycode.kboy.host.io

import android.content.Context
import android.net.Uri

class AndroidFileSystem(
    private val context: Context
) : FileSystem {

    override suspend fun readFile(uri: String): ByteArray {
        return context.contentResolver.openInputStream(Uri.parse(uri)).use {
            it?.readBytes() ?: ByteArray(0)
        }
    }
}
