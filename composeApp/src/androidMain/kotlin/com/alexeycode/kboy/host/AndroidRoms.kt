package com.alexeycode.kboy.host

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AndroidRoms : Roms, ActivityResultCallback<Uri?> {

    private val romUris = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
    private val startRomSelectionEvents = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    override fun selectedRomUri(): Flow<String> {
        return romUris.asSharedFlow()
    }

    override fun selectRom() {
        startRomSelectionEvents.tryEmit(Unit)
    }

    fun startRomSelection(): Flow<Unit> {
        return startRomSelectionEvents
    }

    override fun onActivityResult(result: Uri?) {
        if (result != null) {
            romUris.tryEmit(result.toString())
        }
    }
}