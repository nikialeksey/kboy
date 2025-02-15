package com.alexeycode.kboy.io

interface FileSystem {
    suspend fun readFile(uri: String): ByteArray

    class Dummy : FileSystem {
        override suspend fun readFile(uri: String): ByteArray {
            return ByteArray(0)
        }
    }
}