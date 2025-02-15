package com.alexeycode.kboy.io

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class LoadRomContract : ActivityResultContract<Unit, Uri?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT)
            // .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip", "application/gb"))
            .setType("*/*")
    }

    override fun getSynchronousResult(
        context: Context,
        input: Unit
    ): SynchronousResult<Uri?>? = null

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }

}