package com.poste

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class ScreenState {
    INTRO,
    REGISTER
}

class SharedViewModel: ViewModel() {
    val currentScreenState = mutableStateOf(ScreenState.INTRO)
}

