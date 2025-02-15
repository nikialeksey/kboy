package com.alexeycode.kboy.main.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.onPressRelease(
    onPressed: () -> Unit,
    onRelease: () -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                try {
                    onPressed()
                    awaitRelease()
                } finally {
                    onRelease()
                }
            }
        )
    }
}