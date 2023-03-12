package pro.devdesign.gameboy.cartridge

import org.cactoos.Scalar
import org.cactoos.scalar.Sticky
import pro.devdesign.gameboy.mem.Rom
import java.io.File

class GbCartridgeData : Rom {

    private val data: Scalar<IntArray>

    constructor(file: File) : this(
        Sticky {
            file
                .inputStream()
                .use {
                    it.readAllBytes()
                        .map(Byte::toUByte)
                        .map(UByte::toInt)
                        .toIntArray()
                }
        }
    )

    constructor(clazz: Class<*>, resourceName: String) : this(
        Sticky {
            clazz
                .getResourceAsStream(
                    resourceName
                )
                ?.use {
                    it.readAllBytes()
                        .map(Byte::toUByte)
                        .map(UByte::toInt)
                        .toIntArray()
                }
                ?: throw IllegalArgumentException("Could not find resource with name: $resourceName")
        }
    )

    constructor(data: Scalar<IntArray>) {
        this.data = data
    }

    override fun read8(address: Int): Int {
        return data.value()[address]
    }
}