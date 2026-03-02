package com.alexeycode.kboy.host

interface RomFile {
    suspend fun read(): ByteArray
}