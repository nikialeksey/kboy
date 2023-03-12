package pro.devdesign.gameboy.cartridge

import org.cactoos.Scalar
import org.cactoos.scalar.Sticky

class GbHeader : Header {

    private val logo: Scalar<Logo>
    private val name: Scalar<String>

    constructor(data: IntArray) : this(
        Sticky {
            GbLogo(data.sliceArray(0x04..0x33))
        },
        Sticky {
            data.slice(0x34 until 0x43)
                .dropLastWhile { it == 0x00 }
                .map { it.toByte() }
                .toByteArray()
                .decodeToString()
                .trim()
        }
    )

    constructor(logo: Scalar<Logo>, name: Scalar<String>) {
        this.logo = logo
        this.name = name
    }

    override fun logo(): Logo {
        return logo.value()
    }

    override fun name(): String {
        return name.value()
    }

}