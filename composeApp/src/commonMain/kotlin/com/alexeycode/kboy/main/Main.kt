package com.alexeycode.kboy.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexeycode.kboy.gb.ppu.GbImageBitmap
import com.alexeycode.kboy.gl.RenderImage

data class MainState(
    val romUri: String = ""
)

@Composable
fun Main(viewModel: MainViewModel = viewModel { MainViewModel() }) {
    val state by viewModel.state
    val romUri = remember(state) { state.romUri }

    if (romUri.isNotEmpty()) {
        val image by viewModel.image.collectAsState(GbImageBitmap())
        RenderImage(image)
    } else {
        Button(onClick = {
            viewModel.updateRomUri("C:\\Users\\nikia\\IdeaProjects\\GameBoy\\composeApp\\src\\commonTest\\composeResources\\files\\tetris.gb")
        }) {
            Text("Start")
        }
    }

}