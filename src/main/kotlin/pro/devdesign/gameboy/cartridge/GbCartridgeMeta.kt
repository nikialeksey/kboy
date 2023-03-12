package pro.devdesign.gameboy.cartridge

import pro.devdesign.gameboy.mem.Rom

class GbCartridgeMeta : CartridgeMeta {

    private val data: Rom

    constructor(cartridgeData: Rom) {
        this.data = cartridgeData
    }

    override fun header(): Header {
        return GbHeader((0x100..0x150).map { data.read8(it) }.toIntArray())
    }

    override fun romSizeKb(): Int {
        return 32 * (1 shl data.read8(0x148))
    }

    override fun ramSizeKb(): Int {
        val value = data.read8(0x149)
        return if (value == 0x01) {
            2
        } else if (value == 0x02) {
            8
        } else if (value == 0x03) {
            32
        } else if (value == 0x04) {
            128
        } else if (value == 0x05) {
            64
        } else {
            0
        }
    }
}