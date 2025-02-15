package com.alexeycode.kboy.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexeycode.kboy.gb.ppu.GbScreen
import com.alexeycode.kboy.gb.ppu.SCREEN_HEIGHT
import com.alexeycode.kboy.gb.ppu.SCREEN_WIDTH
import com.alexeycode.kboy.gb.ppu.Screen
import com.alexeycode.kboy.gl.RenderScreen
import com.alexeycode.kboy.host.Host
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.FileSystem
import com.alexeycode.kboy.io.TouchControllerListener
import com.alexeycode.kboy.main.components.AB
import com.alexeycode.kboy.main.components.DPad
import com.alexeycode.kboy.main.components.StartSelect
import kotlinx.coroutines.flow.Flow

data class MainState(
    val touchControllerEnabled: Boolean = false,
    val isGameRunning: Boolean = false
)

@Composable
fun Main(
    host: Host,
    fileSystem: FileSystem,
    extController: Controller,
    viewModel: MainViewModel = viewModel {
        MainViewModel(
            interactor = MainInteractor(
                fileSystem
            ),
            host = host,
            extController = extController
        )
    }
) {
    val state by viewModel.state
    val isTouchEnabled = remember(state) { state.touchControllerEnabled }
    val isGameRunning = remember(state) { state.isGameRunning }
    val onRunClick = remember(state) {
        {
            viewModel.updateRomUri(
                "C:\\Users\\nikia\\IdeaProjects\\GameBoy\\composeApp\\src\\commonTest\\composeResources\\files\\tetris.gb"
            )
        }
    }
    val screen = remember(state) { viewModel.screen }
    val touchListener = remember(state) { viewModel.touchControllerListener() }

    if (isTouchEnabled) {
        GbScreenWithController(
            isGameRunning = isGameRunning,
            screen = screen,
            onRunClick = onRunClick,
            touchControllerListener = touchListener
        )
    } else {
        GbScreen(
            isGameRunning = isGameRunning,
            screen = screen,
            onRunClick = onRunClick
        )
    }
}


@Composable
fun GbScreenWithController(
    isGameRunning: Boolean,
    screen: Flow<Screen>,
    touchControllerListener: TouchControllerListener,
    onRunClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .width(184.dp) // 40 + 40 + 40 + 32*2
                .padding(32.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            DPad(
                onRightPressed = touchControllerListener::rightPressed,
                onRightReleased = touchControllerListener::rightReleased,
                onLeftPressed = touchControllerListener::leftPressed,
                onLeftReleased = touchControllerListener::leftReleased,
                onUpPressed = touchControllerListener::upPressed,
                onUpReleased = touchControllerListener::upReleased,
                onDownPressed = touchControllerListener::downPressed,
                onDownReleased = touchControllerListener::downReleased,
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // screen + select/start
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GbScreen(
                    modifier = Modifier
                        .aspectRatio(SCREEN_WIDTH.toFloat()/SCREEN_HEIGHT.toFloat()),
                    isGameRunning = isGameRunning,
                    screen = screen,
                    onRunClick = onRunClick
                )
            }
            Box {
                StartSelect(
                    onStartPressed = touchControllerListener::startPressed,
                    onStartReleased = touchControllerListener::startReleased,
                    onSelectPressed = touchControllerListener::selectPressed,
                    onSelectReleased = touchControllerListener::selectReleased,
                )
            }
        }
        Column(
            modifier = Modifier
                .width(184.dp)
                .padding(32.dp)

        ) {
            // a/b
            AB(
                onAPressed = touchControllerListener::aPressed,
                onAReleased = touchControllerListener::aReleased,
                onBPressed = touchControllerListener::bPressed,
                onBReleased = touchControllerListener::bReleased,
            )
        }
    }
}

@Composable
private fun GbScreen(
    modifier: Modifier = Modifier,
    isGameRunning: Boolean,
    screen: Flow<Screen>,
    onRunClick: () -> Unit
) {
    Box(modifier = modifier) {
        if (isGameRunning) {
            val screenSnapshot by screen.collectAsState(GbScreen())
            RenderScreen(screenSnapshot)
        } else {
            Button(onClick = { onRunClick() }) {
                Text("Run")
            }
        }
    }
}
