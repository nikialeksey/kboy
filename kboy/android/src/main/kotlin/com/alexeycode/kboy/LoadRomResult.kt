package com.alexeycode.kboy

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import com.alexeycode.kboy.host.AndroidRomFile
import com.alexeycode.kboy.host.SimpleRoms

class LoadRomResult(
    private val applicationContextProvider: () -> Context,
    private val roms: SimpleRoms
) : ActivityResultCallback<Uri?> {

    override fun onActivityResult(result: Uri?) {
        if (result != null) {
            roms.selectedRom(
                AndroidRomFile(applicationContextProvider().contentResolver, result)
            )
        }
    }
}