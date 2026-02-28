package com.alexeycode.kboy.host

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface Roms {
    fun selectedRomUri(): Flow<String>
    fun selectRom()

    class Dummy : Roms {
        override fun selectedRomUri(): Flow<String> {
            return emptyFlow()
        }

        override fun selectRom() {
            // ignored
        }
    }
}