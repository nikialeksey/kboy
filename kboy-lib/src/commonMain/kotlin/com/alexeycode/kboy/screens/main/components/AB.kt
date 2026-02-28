package com.alexeycode.kboy.screens.main.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun AB(
    onAPressed: () -> Unit,
    onAReleased: () -> Unit,
    onBPressed: () -> Unit,
    onBReleased: () -> Unit,
) {

    val aInteractionSource = remember { MutableInteractionSource() }
    val isAPressed by aInteractionSource.collectIsPressedAsState()
    if (isAPressed) { onAPressed() } else { onAReleased() }

    val bInteractionSource = remember { MutableInteractionSource() }
    val isBPressed by bInteractionSource.collectIsPressedAsState()
    if (isBPressed) { onBPressed() } else { onBReleased() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End // TODO RTL
    ) {
        Button(
            interactionSource = aInteractionSource,
            onClick = { }
        ) {
            Text("A")
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start // TODO RTL
    ) {
        Button(
            interactionSource = bInteractionSource,
            onClick = { }
        ) {
            Text("B")
        }
    }
}