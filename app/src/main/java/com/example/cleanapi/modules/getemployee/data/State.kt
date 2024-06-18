package com.example.cleanapi.modules.getemployee.data

sealed class State {
    data object Loading : State()
    data object Success : State()
}

