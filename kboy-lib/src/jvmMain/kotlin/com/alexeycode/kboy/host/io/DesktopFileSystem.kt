package com.alexeycode.kboy.host.io

import java.nio.file.Files
import java.nio.file.Path

class DesktopFileSystem : FileSystem {
    override suspend fun readFile(uri: String): ByteArray {
        return Files.readAllBytes(Path.of(uri))
    }
}
