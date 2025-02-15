package com.alexeycode.kboy.io

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class LoadRomContract : ActivityResultContracts.OpenDocument() {
    override fun createIntent(context: Context, input: Array<String>): Intent {
        return super.createIntent(context, input).apply {
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip", "application/gb"))
        }
    }
}