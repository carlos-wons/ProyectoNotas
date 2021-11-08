package com.example.notas

class Nota(
    var idNota: Int, var titulo: String, var descripcion: String
) {
    constructor(titulo: String, descripcion: String):this(0,titulo, descripcion)

}