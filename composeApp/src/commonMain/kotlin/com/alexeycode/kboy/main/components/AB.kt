package com.alexeycode.kboy.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AB(
    onAPressed: () -> Unit,
    onAReleased: () -> Unit,
    onBPressed: () -> Unit,
    onBReleased: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End // TODO RTL
    ) {
        Button(
            modifier = Modifier.onPressRelease(onAPressed, onAReleased),
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
            modifier = Modifier.onPressRelease(onBPressed, onBReleased),
            onClick = { }
        ) {
            Text("B")
        }
    }
}