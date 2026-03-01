package com.alexeycode.kboy.host

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DesktopRoms : Roms {

    private val selectRomEvents = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    private val selectedRomUri = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)

    override fun selectedRomUri(): Flow<String> {
        return selectedRomUri
    }

    override fun selectRom() {
        selectRomEvents.tryEmit(Unit)
    }

    fun selectRomEvents(): SharedFlow<Unit> {
        return selectRomEvents.asSharedFlow()
    }

    fun selectedRom(uri: String) {
        selectedRomUri.tryEmit(uri)
    }
}
