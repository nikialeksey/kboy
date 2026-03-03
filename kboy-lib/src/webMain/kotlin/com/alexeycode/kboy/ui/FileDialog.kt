package com.alexeycode.kboy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.File

@Composable
fun FileDialog(
    onFilePicked: (File) -> Unit,
    onCompleted: () -> Unit
) {
    val input = remember {
        document.createElement("input") as HTMLInputElement
    }

    LaunchedEffect(Unit) {
        input.style.display = "none"
        input.type = "file"
        input.multiple = false
        input.onchange = { event ->
            try {
                val files = event.target
                    ?.unsafeCast<HTMLInputElement>()
                    ?.files
                    ?.asList()

                if (!files.isNullOrEmpty()) {
                    onFilePicked(files.first())
                }
            } finally {
                document.body?.removeChild(input)
                onCompleted()
            }
        }
        input.oncancel = {
            document.body?.removeChild(input)
            onCompleted()
        }
        document.body?.appendChild(input)

        input.click()
    }

    DisposableEffect(Unit) {
        onDispose {
            document.body?.removeChild(input)
        }
    }
}