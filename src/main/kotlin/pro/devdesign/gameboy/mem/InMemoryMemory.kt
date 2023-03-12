package pro.devdesign.gameboy.mem

class InMemoryMemory : Memory {

    private val data: Array<Int>

    constructor() : this(0xFFFF)

    constructor(size: Int) : this(Array(size) { 0 })

    constructor(data: Array<Int>) {
        this.data = data
    }

    override fun read8(address: Int): Int {
        /**
         * 0000-3FFF   16KB ROM Bank 00     (in cartridge, fixed at bank 00)
         * 4000-7FFF   16KB ROM Bank 01..NN (in cartridge, switchable bank number)
         * 8000-9FFF   8KB Video RAM (VRAM) (switchable bank 0-1 in CGB Mode)
         * A000-BFFF   8KB External RAM     (in cartridge, switchable bank, if any)
         * C000-CFFF   4KB Work RAM Bank 0 (WRAM)
         * D000-DFFF   4KB Work RAM Bank 1 (WRAM)  (switchable bank 1-7 in CGB Mode)
         * E000-FDFF   Same as C000-DDFF (ECHO)    (typically not used)
         * FE00-FE9F   Sprite Attribute Table (OAM)
         * FEA0-FEFF   Not Usable
         * FF00-FF7F   I/O Ports
         * FF80-FFFE   High RAM (HRAM)
         * FFFF        Interrupt Enable Register
         * */
        return data[address]
    }

    override fun write8(address: Int, value: Int) {
        if (address == 0xFF01) {
            print(value.toChar())
        }
        if (address == 0xFF02) {
            println("0x${value.toString(16).uppercase()}")
        }
        data[address] = value
    }
}