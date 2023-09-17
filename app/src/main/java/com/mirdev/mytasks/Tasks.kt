package com.mirdev.mytasks

data class Tasks(
    var id: String ="",
    var name: String = "",
    var isCompleted: Boolean = false
) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this("","", false)
}