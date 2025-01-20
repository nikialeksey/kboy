package com.alexeycode.kboy.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: MainInteractor = MainInteractor()
) : ViewModel() {

    val state = mutableStateOf(MainState())

    fun updateRomUri(romUri:  String) {
        viewModelScope.launch {
            interactor.prepareGb(romUri)

            state.value = state.value.copy(romUri = romUri)
        }
    }

}