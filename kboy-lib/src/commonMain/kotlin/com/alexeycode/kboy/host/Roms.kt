package com.alexeycode.kboy.host

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface Roms {
    fun selectedRomFile(): Flow<RomFile>
    fun selectRom()

    class Dummy : Roms {
        override fun selectedRomFile(): Flow<RomFile> {
            return emptyFlow()
        }

        override fun selectRom() {
            // ignored
        }
    }
}