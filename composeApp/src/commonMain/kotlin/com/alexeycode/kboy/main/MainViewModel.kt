package com.alexeycode.kboy.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeycode.kboy.gb.ppu.ImageBitmap
import com.alexeycode.kboy.io.Controller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: MainInteractor = MainInteractor()
) : ViewModel() {

    private var _image: Flow<ImageBitmap>? = null
    val state = mutableStateOf(MainState())
    val image: Flow<ImageBitmap>
        get() {
            return _image!!
        }

    fun updateRomUri(romUri:  String, controller: Controller) {
        viewModelScope.launch {
            _image = interactor.prepareGb(viewModelScope, romUri, controller)

            state.value = state.value.copy(romUri = romUri)
        }
    }

}