package com.alexeycode.kboy.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeycode.kboy.gb.ppu.Screen
import com.alexeycode.kboy.host.Host
import com.alexeycode.kboy.host.Roms
import com.alexeycode.kboy.io.Controller
import com.alexeycode.kboy.io.GroupController
import com.alexeycode.kboy.io.TouchController
import com.alexeycode.kboy.io.TouchControllerListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: MainInteractor,
    private val roms: Roms,
    private val host: Host,
    private val extController: Controller,
    private val touchController: TouchController = TouchController()
) : ViewModel() {

    private var runJob: Job? = null
    private var _screen: Flow<Screen>? = null
    val screen: Flow<Screen>
        get() {
            return _screen ?: emptyFlow()
        }
    val state = mutableStateOf(MainState())

    init {
        viewModelScope.launch {
            host.externalControllerAvailable().collect { extControllerAvailable ->
                state.value = state.value.copy(touchControllerEnabled = !extControllerAvailable)
            }
        }
        viewModelScope.launch {
            roms.selectedRomUri().collect { romUri: String ->
                updateRomUri(romUri)
            }
        }
    }

    fun touchControllerListener(): TouchControllerListener {
        return touchController
    }

    fun updateRomUri(romUri:  String) {
        runJob?.cancel()
        runJob = viewModelScope.launch {
            _screen = interactor.prepareGb(
                viewModelScope,
                romUri,
                GroupController(
                    listOf(extController, touchController)
                )
            )

            state.value = state.value.copy(isGameRunning = true)
        }
    }

    fun onSelectRomClicked() {
        roms.selectRom()
    }

}