package com.example.diario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Treino(val id: Int, val nome: String)

// ViewModel ÚNICO: criado uma vez na MainActivity e passado como parâmetro
class AppViewModel : ViewModel() {
    var logado by mutableStateOf(false)
        private set

    val treinos = mutableStateListOf<Treino>()
    private var proximoId = 1

    fun login() { logado = true }

    fun adicionar() {
        treinos.add(Treino(proximoId, "Treino $proximoId"))
        proximoId++
    }

    fun limpar() = treinos.clear()
}
