package com.alexeycode.kboy.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class MainState(
    val romUri: String = ""
)

@Composable
fun Main(viewModel: MainViewModel = viewModel { MainViewModel() }) {
    val state by viewModel.state
    val romUri = remember(state) { state.romUri }

    if (romUri.isNotEmpty()) {
        viewModel
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
    } else {

    }

}