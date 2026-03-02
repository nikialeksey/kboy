package com.alexeycode.kboy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Paths

@Composable
fun FileDialog(
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        val parent: Frame? = null
        object : FileDialog(parent, "Choose a ROM", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(Paths.get(directory, file).toFile().absolutePath)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)
