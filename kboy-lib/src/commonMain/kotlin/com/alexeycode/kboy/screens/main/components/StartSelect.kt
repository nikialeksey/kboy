package com.alexeycode.kboy.screens.main.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartSelect(
    onStartPressed: () -> Unit,
    onStartReleased: () -> Unit,
    onSelectPressed: () -> Unit,
    onSelectReleased: () -> Unit
) {
    val startInteractionSource = remember { MutableInteractionSource() }
    val isStartPressed by startInteractionSource.collectIsPressedAsState()
    if (isStartPressed) { onStartPressed() } else { onStartReleased() }

    val selectInteractionSource = remember { MutableInteractionSource() }
    val isSelectPressed by selectInteractionSource.collectIsPressedAsState()
    if (isSelectPressed) { onSelectPressed() } else { onSelectReleased() }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            interactionSource = selectInteractionSource,
            onClick = {  },
        ) {
            Text("Select")
        }
        OutlinedButton(
            interactionSource = startInteractionSource,
            onClick = {  },
        ) {
            Text("Start")
        }
    }
}
