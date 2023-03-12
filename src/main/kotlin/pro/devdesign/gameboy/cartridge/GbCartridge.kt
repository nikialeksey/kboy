package pro.devdesign.gameboy.cartridge

import pro.devdesign.gameboy.mem.Memory
import pro.devdesign.gameboy.mem.Rom

class GbCartridge(
    private val data: Rom,
    private val meta: CartridgeMeta
) : Cartridge {

    constructor(data: Rom) : this(data, GbCartridgeMeta(data))

    override fun upload(to: Memory) {
        for (address in 0x0000..0x7FFF) {
            to.write8(address, data.read8(address))
        }
    }

}