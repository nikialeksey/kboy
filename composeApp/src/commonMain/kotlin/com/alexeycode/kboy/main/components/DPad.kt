package com.alexeycode.kboy.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp

@Composable
fun DPad(
    onRightPressed: () -> Unit,
    onRightReleased: () -> Unit,
    onLeftPressed: () -> Unit,
    onLeftReleased: () -> Unit,
    onUpPressed: () -> Unit,
    onUpReleased: () -> Unit,
    onDownPressed: () -> Unit,
    onDownReleased: () -> Unit,
) {
    // d-pad
    val dPadPath = remember {
        /**
         *       (-1, 3)   (1, 3)
         *              ***
         *      (-1, 1) *** (1, 1)
         *  (-3, 1) *********** (3, 1)
         * (-3, -1) *********** (3, -1)
         *     (-1, -1) *** (1, -1)
         *              ***
         *     (-1, -3)     (1, -3)
         */
        /**
         *       (-1, 3)   (1, 3)
         *              ***
         *      (-1, 1) *** (1, 1)
         *  (-3, 1) *********** (3, 1)
         * (-3, -1) *********** (3, -1)
         *     (-1, -1) *** (1, -1)
         *              ***
         *     (-1, -3)     (1, -3)
         */
        Path()
            .apply {
                moveTo(1f / 3f, 3f / 3f)
                lineTo(1f / 3f, 1f / 3f)
                lineTo(3f / 3f, 1f / 3f)
                lineTo(3f / 3f, -1f / 3f)
                lineTo(1f / 3f, -1f / 3f)
                lineTo(1f / 3f, -3f / 3f)
                lineTo(-1f / 3f, -3f / 3f)
                lineTo(-1f / 3f, -1f / 3f)
                lineTo(-3f / 3f, -1f / 3f)
                lineTo(-3f / 3f, 1f / 3f)
                lineTo(-1f / 3f, 1f / 3f)
                lineTo(-1f / 3f, 3f / 3f)
                close()
            }
    }
    val dPadColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .drawWithCache {
                onDrawBehind {
                    scale(size.width * 0.5f, size.width * 0.5f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(
                                dPadPath,
                                color = dPadColor
                            )
                        }
                    }
                }
            }
            .size(120.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .onPressRelease(onUpPressed, onUpReleased),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .onPressRelease(onRightPressed, onRightReleased),
            )
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .onPressRelease(onLeftPressed, onLeftReleased)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .onPressRelease(onDownPressed, onDownReleased)
            )
        }
    }
}