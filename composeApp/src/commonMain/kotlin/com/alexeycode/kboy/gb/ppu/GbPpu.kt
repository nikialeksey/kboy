package com.alexeycode.kboy.gb.ppu

import androidx.compose.ui.graphics.ImageBitmap
import com.alexeycode.kboy.gb.mem.Memory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GbPpu(
    private val memory: Memory
) : Ppu {

    private val _screen = MutableSharedFlow<ImageBitmap>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val screen = _screen.asSharedFlow()
    private val gbScreen = GbImageBitmap()

    /**
     * $8000-$97FF - memory for tiles, two banks for CGB
     * 
     */
    override fun tick(clockCycles: Int) {
        _screen.tryEmit(gbScreen)
    }

    override fun screen(): SharedFlow<ImageBitmap> {
        return screen
    }

}