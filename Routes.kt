package com.example.diario

import kotlinx.serialization.Serializable

// Rotas principais (type-safe, nenhuma String solta)
sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Login : Route
    @Serializable data object Main : Route
    @Serializable data class Detail(val id: Int) : Route
}

// Rotas das 3 abas do Bottom Navigation
sealed interface Tab {
    @Serializable data object Treinos : Tab
    @Serializable data object Stats : Tab
    @Serializable data object Perfil : Tab
}
