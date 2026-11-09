package com.gamevault.mobile.presentation.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gamevault.mobile.di.AppContainer

fun authViewModelFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
    initializer { AuthViewModel(container.authRepository) }
}

fun gamesViewModelFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
    initializer { GamesViewModel(container.gameRepository) }
}
