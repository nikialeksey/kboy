package com.alexeycode.kboy.gb.ppu

import kotlinx.coroutines.flow.SharedFlow

interface Ppu {

    fun tick(clockCycles: Int)

    fun screen(): SharedFlow<ImageBitmap>
}