package com.alexeycode.kboy.host

import java.nio.file.Files
import java.nio.file.Path

class DesktopRomFile(
    private val uri: String
) : RomFile {

    override suspend fun read(): ByteArray {
        return Files.readAllBytes(Path.of(uri))
    }
}
