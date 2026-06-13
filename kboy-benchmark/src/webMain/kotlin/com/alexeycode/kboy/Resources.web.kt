package com.alexeycode.kboy

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("node:fs")
external object Fs {
    @Suppress("UnusedParameter")
    fun readFileSync(path: String): JsBuffer
}

external class JsBuffer {
    val length: Int

    @JsName("readInt8")
    fun readInt8(offset: Int): Byte
}

actual fun readBytes(fileName: String): ByteArray {
    val buffer = Fs.readFileSync("./src/commonMain/resources/$fileName")

    return ByteArray(buffer.length) {
        buffer.readInt8(it)
    }
}
