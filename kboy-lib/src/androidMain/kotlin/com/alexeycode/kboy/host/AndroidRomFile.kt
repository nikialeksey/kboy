package com.alexeycode.kboy.host

import android.content.ContentResolver
import android.net.Uri

class AndroidRomFile(
    val contentResolver: ContentResolver,
    val uri: Uri
) : RomFile {
    override suspend fun read(): ByteArray {
        return contentResolver.openInputStream(uri).use {
            it?.readBytes() ?: ByteArray(0)
        }
    }
}
