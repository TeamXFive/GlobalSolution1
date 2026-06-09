package com.example.satellitetracker.data

/** Estado de uma tela que carrega dados da API: carregando, sucesso ou erro. */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
