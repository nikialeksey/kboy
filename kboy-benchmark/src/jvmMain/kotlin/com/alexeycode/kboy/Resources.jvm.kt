package com.alexeycode.kboy

import java.io.File

actual fun readBytes(fileName: String): ByteArray {
    val fullFileName = "./src/commonMain/resources/$fileName"
    return File(fullFileName).readBytes()
}
