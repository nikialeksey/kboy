package com.alexeycode.kboy.gb.ppu

import com.alexeycode.kboy.gb.mem.Memory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

private const val RENDER_CYCLES = 70224
private const val LINE_CYCLES = 456
private const val OAM_SCAN_CYCLES = 80

class GbPpu(
    private val memory: Memory,
    private val lcdStatus: LcdStatus
) : Ppu {

    private val _screen = MutableSharedFlow<ImageBitmap>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val screen = _screen.asSharedFlow()
    private val gbScreen = GbImageBitmap()
    private var cycles = 0 // 70224 clock cycles


    override fun tick(clockCycles: Int) {
        /**
         * $8000-$97FF - memory for tiles, two banks for CGB
         */

//        for (x in 0 until 160) {
//            for (y in 0 until 144) {
//                gbScreen.setPixel(x, y, 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte())
//            }
//        }
        gbScreen.setPixel(0, 0, 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte())
        gbScreen.setPixel(1, 0, 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte())

        _screen.tryEmit(gbScreen)
    }

    override fun screen(): SharedFlow<ImageBitmap> {
        return screen
    }

}