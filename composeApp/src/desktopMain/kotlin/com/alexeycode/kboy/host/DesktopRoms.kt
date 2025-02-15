package com.alexeycode.kboy.host

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DesktopRoms : Roms {
    override fun selectedRomUri(): Flow<String> {
        return flowOf(
            "C:\\Users\\nikia\\IdeaProjects\\GameBoy\\composeApp\\src\\commonTest\\composeResources\\files\\tetris.gb"
        )
    }

    override fun selectRom() {
        // TODO
    }
}