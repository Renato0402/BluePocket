package com.example.bluepocket.entity

import java.util.*

data class Receita(
    var nome: String = "",
    var tipo: String = "",
    var data: Long = 0,
    var valor: Double = 0.0,
    var typeId: String = ""
)