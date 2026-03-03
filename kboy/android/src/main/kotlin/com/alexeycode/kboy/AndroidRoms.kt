package com.alexeycode.kboy

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import com.alexeycode.kboy.host.AndroidRomFile
import com.alexeycode.kboy.host.RomFile
import com.alexeycode.kboy.host.Roms
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AndroidRoms(
    private val applicationContextProvider: () -> Context
) : Roms, ActivityResultCallback<Uri?> {

    private val romUris = MutableSharedFlow<RomFile>(0, 1, BufferOverflow.DROP_OLDEST)
    private val startRomSelectionEvents = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    override fun selectedRomFile(): Flow<RomFile> {
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
            romUris.tryEmit(AndroidRomFile(applicationContextProvider().contentResolver, result))
        }
    }
}