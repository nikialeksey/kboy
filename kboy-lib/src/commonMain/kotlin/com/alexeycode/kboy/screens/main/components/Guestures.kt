package com.alexeycode.kboy.screens.main.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.onPressRelease(
    onPressed: () -> Unit,
    onRelease: () -> Unit
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val localIndication = LocalIndication.current

    val isPressed by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        onPressed()
    } else {
        onRelease()
    }

    Modifier.clickable(
        interactionSource = interactionSource,
        indication = localIndication
    ) {
        // on clicked
    }
}