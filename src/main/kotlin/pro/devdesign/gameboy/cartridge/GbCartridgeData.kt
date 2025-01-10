package pro.devdesign.gameboy.cartridge

import pro.devdesign.gameboy.mem.Rom
import java.io.File
import java.io.InputStream

class GbCartridgeData : Rom {

    private val data by lazy {
        dataStream
            .use {
                it.readAllBytes()
                    .map(Byte::toUByte)
                    .map(UByte::toInt)
                    .toIntArray()
            }
    }
    private val dataStream: InputStream

    constructor(file: File) : this(file.inputStream())

    constructor(clazz: Class<*>, resourceName: String) : this(
            clazz
                .getResourceAsStream(
                    resourceName
                )
                ?: throw IllegalArgumentException("Could not find resource with name: $resourceName")
    )

    constructor(dataStream: InputStream) {
        this.dataStream = dataStream
    }

    override fun read8(address: Int): Int {
        return data[address]
    }
}