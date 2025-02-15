package com.alexeycode.kboy.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartSelect(
    onStartPressed: () -> Unit,
    onStartReleased: () -> Unit,
    onSelectPressed: () -> Unit,
    onSelectReleased: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            modifier = Modifier
                .onPressRelease(onSelectPressed, onSelectReleased),
            onClick = {  },
        ) {
            Text("Select")
        }
        OutlinedButton(
            modifier = Modifier
                .onPressRelease(onStartPressed, onStartReleased),
            onClick = {  },
        ) {
            Text("Start")
        }
    }
}
